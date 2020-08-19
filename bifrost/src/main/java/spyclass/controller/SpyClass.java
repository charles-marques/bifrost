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

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

import conceptnet.client.api.query.ConceptQuery;
import spyclass.Classe;
import spyclass.Resultado;

public class SpyClass {

	private static final String SKIN_COLOR = "skinColor";
	private static final String RACE = "race";
	private static final String HEIGHT = "height";
	private static final String WEIGHT = "weight";
	private static final String BDAY = "bday";
	private static final String BIRTHDAY = "birthday";
	private static final String AGE = "age";
	private static final String GENDER = "gender";
	private static final String SEX = "sex";
	private static final String PWD = "pwd";
	private static final String PASSWORD = "password";
	private static final String NICK = "nick";
	private static final String NICK_NAME = "nickName";
	private static final String FULL_NAME = "fullName";
	private static final String LAST_NAME = "lastName";
	private static final String FIRST_NAME = "firstName";
	private static final String NAME = "name";
	private static final String QUEBRA_LINHA = "\n";
	private static final String VIRGULA = ",";
	private static final String PONTO = ".";
	private static final String SRC = "src";
	private static final String JAVA = ".java";
	private static final String UNDERSCORE = "_";
	private static final String VAZIO = "";
//	private final List<String> nomesProdurados = Arrays.asList(NAME, NICK, "username", FIRST_NAME, FULL_NAME);
//	private final List<String> atributosProcurados = Arrays.asList(AGE, SEX, BIRTHDAY, PASSWORD);
//	private static List<String> atributosProcurados = Arrays.asList("contactName","school");
	private static List<String> atributosProcurados = new ArrayList<String>();

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
			ClassOrInterfaceDeclaration instancia = classe.get();
			List<FieldDeclaration> fields = instancia.getFields();
//			Boolean name = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(NAME.toLowerCase()));
//			Boolean firstName = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(FIRST_NAME.toLowerCase()));
//			Boolean lastName = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(LAST_NAME.toLowerCase()));
//			Boolean fullName = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(FULL_NAME.toLowerCase()));
//			Boolean nickName = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(NICK_NAME.toLowerCase()));
//			Boolean nick = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(NICK.toLowerCase()));
//			Boolean password = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(PASSWORD.toLowerCase()));
//			Boolean pwd = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(PWD.toLowerCase()));
//			Boolean sex = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(SEX.toLowerCase()));
//			Boolean gender = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(GENDER.toLowerCase()));
//			Boolean age = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(AGE.toLowerCase()));
//			Boolean birthday = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(BIRTHDAY.toLowerCase()));
//			Boolean bday = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(BDAY.toLowerCase()));
//			Boolean weight = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(WEIGHT.toLowerCase()));
//			Boolean height = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(HEIGHT.toLowerCase()));
//			Boolean race = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(RACE.toLowerCase()));
//			Boolean skinColor = fields.stream().anyMatch(f -> f.getVariables().get(0).getNameAsString().toLowerCase().equals(SKIN_COLOR.toLowerCase()));
//			String atributos = fields.stream().map(f -> f.getVariables().get(0).getNameAsString())
//					.collect(Collectors.joining("; ", "[", "]"));
//			resultado.addClass(new Classe(node.getName(), node.getAbsolutePath(), Boolean.TRUE, name, firstName,
//					lastName, fullName, nickName, nick, password, pwd, sex, gender, age, birthday, bday, weight, height,
//					race, skinColor, atributos));
			resultado.addClass(new Classe(className, node.getAbsolutePath(), Boolean.TRUE));
		} catch (IOException e) {
//			e.printStackTrace();
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
	
	private Boolean isRepresentative(File node) throws FileNotFoundException, IOException {
		String className = node.getName().replace(JAVA, VAZIO);
		if (className.equals("Player")) {
			System.out.println(className);
		}
		try {
			List<String> termos = splitTerms(className);
			if (termos.stream().anyMatch(termo -> conceptQuery.isAPerson(termo)) ) {
				CompilationUnit compilationUnit = getCompilationUnit(node);
				Optional<ClassOrInterfaceDeclaration> classe = compilationUnit.getClassByName(className);
				ClassOrInterfaceDeclaration instancia = classe.get();
//				Stream<FieldDeclaration> fields = instancia.getFields().stream();
//				// versão dinâmica da ConceptNet
//				return fields.anyMatch(f -> splitTerms(f.getTokenRange().toString()).stream().anyMatch(a -> conceptQuery.personHasA(a)));
//				// versão estatica
//				Optional<FieldDeclaration> fieldPrincipal = fields
//						.filter(f -> nomesProdurados.stream()
//								.anyMatch(n -> f.getTokenRange().toString().toLowerCase().contains(n.toLowerCase())) &&
//								!f.getTokenRange().toString().startsWith(UNDERSCORE)
//								)
//						.findFirst();
//				if (!fieldPrincipal.isPresent())
//					return false;
				boolean possuiAttibutoProcurado = instancia.getFields().stream().anyMatch(f -> atributosProcurados
						.stream().anyMatch(n -> f.getTokenRange().toString().toLowerCase().contains(n.toLowerCase())));
				return possuiAttibutoProcurado;
//				return true;
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

	private static String projectPath(Classe c) {
		List<String> arrayPath = Arrays.asList(c.getClassPath().split(File.separator));
		int indice = 0;
		for (int i = arrayPath.size() - 1; i > 0; i--) {
			if (arrayPath.get(i).equals(SRC)) {
				indice = i;
			}
		}
		List<String> arrayProjectPath = arrayPath.subList(0, indice);
		String projectPath = String.join(File.separator, arrayProjectPath);
		return projectPath;
	}
	
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
	
	private void loadProperties(String configFile) {
		String line = VAZIO;
		try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
			while ((line = br.readLine()) != null) {
				if (line.trim().replace(QUEBRA_LINHA, VAZIO) == "") continue;
				atributosProcurados.add(line.replace(QUEBRA_LINHA, VAZIO));
			}
		} catch (IOException e) {
		}
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
