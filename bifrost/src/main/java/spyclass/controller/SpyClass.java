package spyclass.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.google.gson.Gson;

import conceptnet.client.api.query.ConceptQuery;
import spyclass.Classe;
import spyclass.Resultado;
import spyclass.SearchParams;

public class SpyClass {
	private static final String VERIFYING_FIELD = "Verifying field: ";
	private static final String INSPECTING_CLASS = "Inspecting class: ";
	private static final String PERSON = "person";
	private static final String QUEBRA_LINHA = "\n";
	private static final String VIRGULA = ",";
	private static final String PONTO = ".";
	private static final String JAVA = ".java";
	private static final String VAZIO = "";
	private static SearchParams searchParams;

	private Resultado resultado;
	private ConceptQuery conceptQuery = new ConceptQuery();
	
	public Resultado getResultado() {
		return resultado;
	}
	
	public Boolean contains() {
		return false;
	}
	public void addClasse(File node) {
		try {
			String className = node.getName().replace(JAVA, VAZIO);
			CompilationUnit compilationUnit = getCompilationUnit(node);
			Optional<ClassOrInterfaceDeclaration> classe = compilationUnit.getClassByName(className);
			
			resultado.addClass(new Classe(classe.get().getFullyQualifiedName().get(), node.getAbsolutePath(), Boolean.TRUE));
		} catch (IOException e) {
		}
		return;
	}

	public void searchClasses(File node) throws Exception {
		// VERIFY: IS CLASS NAMED WITH ONE OF CLASSES NAMES?
		if (node.isFile() && node.getPath().endsWith(JAVA)) {
			try {
				if (isRepresentative(node)) {
					addClasse(node);
				}
			} catch (Exception e) {
//				e.printStackTrace();
			}
			return;
		} else if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				searchClasses(new File(node, filename));
			}
		} else {
			// NÃO É ARQUIVO '.JAVA'
		}
		return;
	}

	public List<String> splitTerms(String description) {
		return (description == null) ? null : Arrays.asList(description.split("(?=\\p{Upper})"));
	}
	
	public String camelToSnake(String str) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1_$2";
		
		return str.replaceAll(regex, replacement).toLowerCase();
	}
	
	private Boolean isRepresentative(File node) throws FileNotFoundException, IOException {
		String className = node.getName().replace(JAVA, VAZIO);
		System.out.println(INSPECTING_CLASS + className);
		try {
			List<String> termos = splitTerms(className);
			if (termos.stream().anyMatch(termo -> conceptQuery.isAPerson(termo)) ) {
				
				if (searchParams.getQtdAttributes() == 0) return true;
				
				CompilationUnit compilationUnit = getCompilationUnit(node);
				Optional<ClassOrInterfaceDeclaration> classe = compilationUnit.getClassByName(className);
				ClassOrInterfaceDeclaration instancia = classe.get();
				
				List<FieldDeclaration> propertiesInList = new ArrayList<FieldDeclaration>();
				
				if (searchParams.getAttributes() != null && !searchParams.getAttributes().isEmpty()) {
//					boolean possuiAttibutoProcurado = instancia.getFields().stream().anyMatch(f -> searchParams.getAttributes()
//							.stream().anyMatch(n -> f.getTokenRange().toString().toLowerCase().contains(n.toLowerCase())));
					
//					propertiesInList = instancia.getFields().stream()
//							.filter(f -> searchParams.getAttributes().stream().anyMatch(
//									n -> f.getTokenRange().toString().toLowerCase().contains(n.toLowerCase())))
//							.collect(Collectors.toList());
					for (FieldDeclaration fieldDeclaration : instancia.getFields()) {
						System.out.println(VERIFYING_FIELD + fieldDeclaration.getVariables().get(0).toString().toLowerCase());
						for (String attr : searchParams.getAttributes()) {
							if (fieldDeclaration.getVariables().get(0).toString().toLowerCase().equals(attr.toLowerCase())) {
								propertiesInList.add(fieldDeclaration);
							}
						}
					}
				} else {
					propertiesInList = instancia.getFields().stream().filter(f -> conceptQuery.personHasA(camelToSnake(f.getTokenRange().toString()) ) ).collect(Collectors.toList());
				}
				
				if (searchParams.getQtdAttributes() < 3) {
					return searchParams.getQtdAttributes().equals(propertiesInList.size());
				} else {
					return searchParams.getQtdAttributes() > 2 && propertiesInList.size() > 2;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private CompilationUnit getCompilationUnit(File node) throws FileNotFoundException, IOException {
		FileInputStream inputStream = new FileInputStream(node);
		CompilationUnit compilationUnit = StaticJavaParser.parse(inputStream);
		return compilationUnit;
	}

	/**
	 * @param args = new String[] {
	 *             "/home/suporte/Workspace/BaiasOrdenacao/codemining/src/main/java/repository"};
	 *             args = new String[] {
	 *             "/home/suporte/Workspace/BaiasOrdenacao/code-example" }; args =
	 *             new String[] { "/home/suporte/Documentos/java_projects/cafeweb"
	 *             }; args = new String[] {
	 *             "/home/suporte/Documentos/java_projects/networkTalk" }; args =
	 *             new String[] {
	 *             "/home/suporte/Documentos/java_projects/the-k-network/the-k-network"
	 *             };
	 */
	public void inspect(String projeto) {
		if (projeto == null || projeto.trim().isEmpty()) {
			System.err.println("Nenhum diretório informado");
			return;
		} else {
		}
		try {
			resultado = new Resultado();
			searchClasses(new File(projeto));
		} catch (Exception e) {

		} finally {
		}
	}

//	private static String projectPath(Classe c) {
//		List<String> arrayPath = Arrays.asList(c.getClassPath().split(File.separator));
//		int indice = 0;
//		for (int i = arrayPath.size() - 1; i > 0; i--) {
//			if (arrayPath.get(i).equals(SRC)) {
//				indice = i;
//			}
//		}
//		List<String> arrayProjectPath = arrayPath.subList(0, indice);
//		String projectPath = String.join(File.separator, arrayProjectPath);
//		return projectPath;
//	}
	
	public String canonicalName(Classe c) {
		try {
			File node = new File(c.getClassPath());
			String className = node.getName().replace(JAVA, VAZIO);
			CompilationUnit compilationUnit;
			compilationUnit = getCompilationUnit(node);
			return compilationUnit.getPackageDeclaration().get().getNameAsString() + PONTO + className;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String();
	}
	
	private void loadProperties(String configFile) throws Exception {
		try {
			BufferedReader conf;
			conf = new BufferedReader(new FileReader(configFile));
			Gson gson = new Gson();
			searchParams = gson.fromJson(conf, SearchParams.class);
			System.out.println(searchParams);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
//		String line = VAZIO;
//		try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
//			int i = 0;
//			while ((line = br.readLine()) != null) {
//				if (i == 0) continue;
//				if (line.trim().replace(QUEBRA_LINHA, VAZIO) == "") continue;
//				atributosProcurados.add(line.replace(QUEBRA_LINHA, VAZIO));
//				i = i+1;
//			}
//		} catch (IOException e) {
//		}
	}
	
	public static void run(String rootPath, String configFile) {
		try {
			SpyClass spy = new SpyClass();
			String arquivoSaida = "person_class.csv";
			PrintWriter writer = new PrintWriter(arquivoSaida);
			writer.write("project_name" + VIRGULA +"person_class" + QUEBRA_LINHA);
			spy.loadProperties(configFile);
			spy.inspect(rootPath);
			if (!spy.getResultado().getClassesList().isEmpty()) {
				List<Classe> classes = spy.getResultado().getClassesList();
				try {
					classes.forEach(c -> {
						writer.write(rootPath + VIRGULA + c.getClassName()
								+ QUEBRA_LINHA
								);
					});
				} catch (NoSuchElementException e) {
				} catch (ParseProblemException e) {
				}
			}
			System.out.println("============================================================================");
			System.out.println("Writing file...");
			writer.close();
//			System.out.println("End!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
