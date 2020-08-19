package spyclass.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonProjectFinder {

	private static final String SRC_MAIN_JAVA = File.separator + "src"+File.separator+"main"+File.separator+"java"+File.separator;
	private static final String PONTO = ".";
	private static final String SRC = "src";
	private final Character QUEBRA_LINHA = '\n';
	private final static String JAVA = ".java";
	private final static CharSequence VAZIO = "";
	private final List<String> classesProcuradas = Arrays.asList("person", "employee", "customer", "client", "user",
			"pessoa", "empregado", "cliente", "usuario");
	private final List<String> nomesProdurados = Arrays.asList("name", "nome", "nick", "username", "firstName",
			"fullName");
	private final List<String> atributosProcurados = Arrays.asList("age", "sex", "birthday", "password", "idade",
			"sexo", "nascimento", "contato", "senha");

	private List<String> resultado;

	public List<String> getResultado() {
		return resultado;
	}
	
	public void addResultado(String result) {
		if (resultado.contains(result) || result == null) return;
		resultado.add(result);
	}

	public void searchClasses(File node) throws Exception {
		// VERIFY: IS CLASS NAMED WITH ONE OF CLASSES NAMES?
		if (node.isFile() && node.getPath().endsWith(JAVA)) {
			String className = node.getName().replace(JAVA, VAZIO);
			if (classesProcuradas.stream().parallel()
					.anyMatch(cp -> className.toLowerCase().equals(cp.toLowerCase()))) {
				addResultado(projectPath(node.getAbsolutePath()));
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
			resultado = new ArrayList<String>();
			searchClasses(new File(projeto));
//			resultado.writeCsvFiles();
		} catch (Exception e) {

		} finally {
		}
	}

	private String projectPath(String path) {
		if (!path.contains(SRC_MAIN_JAVA)) return null;
		List<String> arrayPath = Arrays.asList(path.split(File.separator));
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

	private List<String> loadClassesDeInteresse(String csvClassesPath) {
		List<String> projetos = new ArrayList<String>();
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(csvClassesPath))) {
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String linha = line.replace("\n", "");
				projetos.add(linha);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projetos;
	}
	
	public static void main(String[] args) {
		try {
//			String rootPath = "/home/suporte/Documentos/java_projects/";
//			String rootPath = "/home/suporte/Workspace/Teste/projetos";
//			File root = new File(rootPath);
			PersonProjectFinder spy = new PersonProjectFinder();
			List<String> excessoes = Arrays.asList("warlock2", "sojo", "JTwitter", "prjReborn", "head",
					"picketbox-solder", "cometd", "chililog-server", "gisgraphy-mirror", "eXist-1.4.x", "netty",
					"abiquo", "VT-Class-Scheduler", "classpath");
			PrintWriter writer = new PrintWriter("estatistica_geral_projetos_java_com_pessoa.csv");
			List<String> projectList = spy.loadClassesDeInteresse("/home/suporte/Workspace/BaiasOrdenacao/spyclass/estatistica_geral_projetos_java.csv");
			int i = 0;
			for (String projeto : projectList) {
				i = i + 1;
				System.out.printf("%5s - %s \n", i, projeto);
				if (excessoes.contains(projeto))
					continue;
				spy.inspect(projeto);
				if (spy.getResultado().isEmpty()) continue;
				spy.getResultado().forEach(r -> {
					writer.write(r + "\n");
				});
			}
			System.out.println("============================================================================");
			System.out.println("Escrevendo arquivo...");
			writer.close();
			System.out.println("Fim!");
		} catch (Exception e) {

		}
	}
}
