package discoverypattern.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import discoverypattern.Classe;

public class Discovery {
	private static final String BARRA = " / ";
	private static final String PROJETO_N = " Projeto n° ";
	private static final String VAZIO = "";
	private static final String QUEBRA_LINHA = "\n";
	private static final String VARIAVEL = "METHOD VARIABLE";
	private static final String PARAMETRO = "METHOD PARAMETER";
	private static final String CLASSE = "CLASS ATTRIBUTE";
	private static final String VIRGULA = ",";
	private static final String JAVA = ".java";
	private final String MSG_EXISTE_CLASSE = "Não foi possível identificar, já existe uma classe List, em pacote java.util, declarada no projeto que não é da JDK.";
	private final String CHAVE_FECHANDO = ">";
	private final String LIST_TYPE = "List<";
	private List<Classe> resultado = new ArrayList<Classe>();

	private List<String> listaJars = new ArrayList<String>();
	private List<String> listaZip = new ArrayList<String>();
	private List<String> listaWar = new ArrayList<String>();
	private List<String> listaEar = new ArrayList<String>();
	private List<String> listaRar = new ArrayList<String>();

	private List<String> atributosDeClasseProcurados = new ArrayList<String>();
//	private List<CompilationUnit> compilationUnitList = new ArrayList<CompilationUnit>();

	private void addResultado(Classe classe) throws Exception {
		if (resultado.contains(classe))
			return;
		resultado.add(classe);
		return;
	}

	private void addAtributoProcurado(String atributo) {
		atributosDeClasseProcurados.add(atributo);
	}
	
	public List<Classe> getResultado() {
		return resultado;
	}

	/**
	 * busca no projeto os recuros do tipo jar,rar,war, zip e ear
	 */
	private void buscaRecursos(String endereco) {
		File root = new File(endereco);
		if (root.isDirectory()) {
			String[] enderecos = root.list();
			for (String filho : enderecos) {
				buscaRecursos(filho);
			}
		} else {
			if (endereco.endsWith(".jar")) {
				listaJars.add(endereco);
			}
			if (endereco.endsWith(".zip")) {
				listaZip.add(endereco);
			}
			if (endereco.endsWith(".rar")) {
				listaRar.add(endereco);
			}
			if (endereco.endsWith(".ear")) {
				listaEar.add(endereco);
			}
			if (endereco.endsWith(".war")) {
				listaWar.add(endereco);
			}
		}

	}

	private void verificarRecursosExternos(String path) throws Exception {
		buscaRecursos(path);
		JarLoad jarLoad = new JarLoad();
		ZipLoad zipLoad = new ZipLoad();

		// verificar a existência de uma classe List em um pacote java.util;
		// - verificar os recursos .jar, .zip, .rar, .war e .ear
		for (String jar : listaJars) {
			if (jarLoad.contains(jar, List.class.getCanonicalName())) {
				throw new Exception(MSG_EXISTE_CLASSE);
			}
		}
		for (String zip : listaZip) {
			if (zipLoad.contains(zip, List.class.getCanonicalName())) {
				throw new Exception(MSG_EXISTE_CLASSE);
			}
		}
		if (!listaEar.isEmpty() || !listaRar.isEmpty() || !listaWar.isEmpty()) {
			throw new Exception(
					"Não foi possível identificar: recurso (.ear, .rar ou .war) encontrado pode conter outra implementação da java.util.List da JDK.");
		}
	}

	private void inspect(String pathProjeto, String classe) {
		resultado = new ArrayList<Classe>();
		try {
			if (pathProjeto == null || classe == null) {
				throw new Exception(
						"Parametros não informados: deve ser informado o projeto e arquivo de classes de interesse ");
			}
			// adicinar opção de identificação a classe alvo:
			// - adicionar o SpyClass aqui para uma forma padrão caso não seja informada a
			// classe pessoa ou o usuário não saiba

			File javaProject = new File(pathProjeto);
			if (!javaProject.isDirectory()) {
				throw new FileNotFoundException("Endereço informado não é um diretório/projeto.");
			}
			
//			loadClassesDeInteresse(args[1]);
			verificarRecursosExternos(javaProject.getAbsolutePath());

			// Load Project Source Code
//			URI uri = javaProject.toURI();
//			Path projectRoot = Paths.get(uri);
//			SourceRoot sourceRoot = new SourceRoot(projectRoot);
			// verificar a existência de uma classe List em um pacote java.util no
			// próprio projeto;
//			verificarRecursosInterno(parseResults);

			// SETUP
			String[] projetoArray = javaProject.getPath().split(File.separator);
			String projeto = projetoArray[projetoArray.length - 1];
			String[] classeInteresse = classe.split("\\.");
			int tamanho = classeInteresse.length;
			String nomeClasseInteresse = classeInteresse[tamanho - 1];
			
			searchParseResults(projeto, nomeClasseInteresse, javaProject);

		} catch (IOException e) {
		} catch (Exception e) {
		}
	}

	private void analisarCompilationUnit(String projeto, String nomeClasseInteresse, File node) throws FileNotFoundException {
		FileInputStream inputStream = new FileInputStream(node);
		CompilationUnit cu = StaticJavaParser.parse(inputStream);
		String classeLocal = node.getName().replace(".java", "");
		Optional<ClassOrInterfaceDeclaration> classe = cu.getClassByName(classeLocal);
		ClassOrInterfaceDeclaration instancia = classe.get();
		verifyFields(instancia, nomeClasseInteresse);
		// verificar métodos:
		List<MethodDeclaration> lista = instancia.getMethods();
		lista.forEach(md -> inspectMethods(projeto, nomeClasseInteresse, classeLocal, md));
	}

	public void searchParseResults(String projeto, String nomeClasseInteresse, File node) throws Exception {
		// VERIFY: IS JAVA CLASS
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				searchParseResults(projeto, nomeClasseInteresse, new File(node, filename));
			}
		} else if (node.isFile() && node.getPath().endsWith(JAVA)) {
			System.out.println("Analisando classe: " + node.getName());
			try {
				analisarCompilationUnit(projeto, nomeClasseInteresse, node);
			} catch (ParseProblemException e) {
			} catch (java.util.NoSuchElementException e) {
			}
			return;
		} else {
			// NÃO É ARQUIVO '.JAVA'
		}
		return;
	}
	
	private void verifyFields(ClassOrInterfaceDeclaration instancia, String nomeClasseInteresse) {
		List<FieldDeclaration> lista = instancia.findAll(FieldDeclaration.class).stream()
				.filter(fd -> fd.getElementType().asString().equals(LIST_TYPE + nomeClasseInteresse + CHAVE_FECHANDO))
				.collect(Collectors.toList());
		if (lista.isEmpty())
			return;
		for (FieldDeclaration n : lista) {
			FieldDeclaration fieldDeclaration = n.asFieldDeclaration();
			addAtributoProcurado(fieldDeclaration.getVariables().get(0).getNameAsString());
		}
		;
	}

	/**
	 * @param identificador
	 * @return * Exemplos de ordenação: "Collections.sort(" ${IDENTIFYER}");"
	 *         Collections.sort(${IDENTIFYER}"," ${IDENTIFYER}".sort("
	 */
	private List<String> ordenacoes(String identificador) {
		return Arrays.asList(
				"Collections.sort(" + identificador + ");", 
				"Collections.sort(" + identificador + ",",
				"Collections.sort(" + identificador + " ");
	}
	private String ordenacaoNativa(String identificador) {
		return identificador + ".sort(";
	}
	

	private void inspectMethods(String projeto, String nomeClasseInteresse, String classeLocal,
			MethodDeclaration methodDeclaration) {
		// lista de parametros do tipo List<ClasseDeInteresse>
		List<Parameter> parametros = methodDeclaration.findAll(Parameter.class).stream()
				.filter(p -> p.getTypeAsString().equals(LIST_TYPE + nomeClasseInteresse + CHAVE_FECHANDO))
				.collect(Collectors.toList());
		
		List<Parameter> parametrosProcurados = parametros.stream()
				.filter(p -> !atributosDeClasseProcurados.contains(p.getNameAsString())).collect(Collectors.toList());
		
		// verificar a existência de variaveis List<ClasseDeInteresse>
		List<VariableDeclarator> todas = methodDeclaration.findAll(VariableDeclarator.class);
		String tipo = LIST_TYPE + nomeClasseInteresse + CHAVE_FECHANDO;
		List<VariableDeclarator> variaveis = todas.stream().filter(p -> p.getType().toString().equals(tipo)).collect(Collectors.toList());
		List<VariableDeclarator> variaveisProcuradas = variaveis.stream()
				.filter(v -> !atributosDeClasseProcurados.contains(v.getNameAsString())).collect(Collectors.toList());
		
		// verificar a existência de atributos de classe List<ClasseDeInteresse>
		List<String> atributosProcurados = atributosDeClasseProcurados.stream()
				.filter(a -> !parametros.stream().anyMatch(p -> p.getNameAsString().equals(a))
						&& !variaveis.stream().anyMatch(v -> v.getNameAsString().equals(a)))
				.collect(Collectors.toList());
		
		if (methodDeclaration.getBody().isPresent()) {
			BlockStmt blockStmt = methodDeclaration.getBody().get();
			// verificar a existência de ordenações em algum dos atributos, parametros
			// ou variaveis no corpo do método
			
			List<Statement> listaBlocos = new ArrayList<Statement>();
			
			listaBlocos = methodDeclaration
					.findAll(Statement.class).stream().filter(
							b -> variaveisProcuradas.stream()
									.anyMatch(v -> ordenacoes(v.getNameAsString()).stream()
											.anyMatch(o -> b.toString().contains(o) || b.toString().contains(ordenacaoNativa(o)) )))
					.collect(Collectors.toList());
			if (!listaBlocos.isEmpty()) {
				Collections.sort(listaBlocos, new Comparator<Statement>() {
					@Override
					public int compare(Statement o1, Statement o2) {
						return Integer.valueOf(o2.getBegin().get().line).compareTo(o1.getBegin().get().line);
					}
				});
			}
			if (listaBlocos.size() > 1) {
				listaBlocos = listaBlocos.subList(0, 1);
			}
			listaBlocos.forEach(b ->
				variaveisProcuradas.forEach(v -> {
					String vname = v.getNameAsString();
					List<String> ordenacoes = ordenacoes(vname);
					if (ordenacoes.stream().anyMatch(o -> b.toString().contains(o) || b.toString().contains(ordenacaoNativa(o))) ) {
						try {
							addResultado(new Classe(projeto, nomeClasseInteresse, classeLocal, b.getBegin().get().line,
									VARIAVEL, v.getNameAsString()));
						} catch (Exception e) {
						}
					}
				})
			);
			
			blockStmt.getStatements().forEach(s -> {
				atributosProcurados.forEach(a -> {
					List<String> ordenacoes = ordenacoes(a);
					if (ordenacoes.stream().anyMatch(o -> s.toString().contains(o) || s.toString().startsWith(ordenacaoNativa(a)))) {
						try {
							addResultado(new Classe(projeto, nomeClasseInteresse, classeLocal, s.getBegin().get().line,
									CLASSE, a));
						} catch (Exception e) {
						}
					}
				});
				parametrosProcurados.forEach(p -> {
					String pname = p.getNameAsString();
					List<String> ordenacoes = ordenacoes(pname);
					if (ordenacoes.stream().anyMatch(o -> s.toString().startsWith(o))) {
						try {
							addResultado(new Classe(projeto, nomeClasseInteresse, classeLocal, s.getBegin().get().line,
									PARAMETRO, p.getNameAsString()));
						} catch (Exception e) {
						}
					}
				});
			});
		}
		;

	}
	
	private List<String[]> loadPersonClass() {
		List<String[]> projetos = new ArrayList<String[]>();
		String line = VAZIO;
		try (BufferedReader br = new BufferedReader(new FileReader("person_class.csv"))) {
			while ((line = br.readLine()) != null) {
				projetos.add(line.replace(QUEBRA_LINHA, VAZIO).split(VIRGULA));
			}
		} catch (IOException e) {
		}
		return projetos;
	}
	
	public static void run(String repositorio) {
		Discovery discovery = new Discovery();
		List<Classe> dados = new ArrayList<Classe>();
		List<String[]> dataset = discovery.loadPersonClass();
		
		for (String[] data : dataset) {
			discovery.inspect(repositorio, data[1]);
			
			dados.addAll(discovery.getResultado());
		}
		
		System.out.println("Writing results...");
		try {
			PrintWriter writer = new PrintWriter("sorting_result.csv");
			writer.write("projet_name" + VIRGULA + "person_class" + VIRGULA + "local_sort_class" + VIRGULA + "line_of_sort" + VIRGULA + "variable_scope" + VIRGULA + "variable_name" + QUEBRA_LINHA);
			dados.forEach(r -> {
				writer.write(r.getProjetName() + VIRGULA + r.getInterestedClass() + VIRGULA + r.getLocalSortClass() + VIRGULA + r.getLine() + VIRGULA + r.getVariableScope() + VIRGULA + r.getStatement() + QUEBRA_LINHA);
			});
			writer.close();
		} catch (FileNotFoundException e) {
		}
	}
}