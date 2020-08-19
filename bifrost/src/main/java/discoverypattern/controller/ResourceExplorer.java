package discoverypattern.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceExplorer {
	private static final Object SRC = "src";
	private static final String JAVA = ".java";
	private List<String> projetos = new ArrayList<String>();
	
	private static String projectPath(String path) {
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
	
	private void addProjeto(String path) {
		if (projetos.contains(path)) return;
		projetos.add(path);
	}
	
	private void searchClasses(File node) {
		// VERIFY: IS CLASS NAMED WITH ONE OF CLASSES NAMES?
		if (node.isFile() && node.getPath().endsWith(JAVA)) {
			addProjeto(projectPath(node.getAbsolutePath()));
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
	
	public void explore(String repositorio) {
		searchClasses(new File(repositorio));
	}
	
	public static void main(String[] args) {
		args = new String[] {"/home/suporte/Workspace/Teste/dataset375_sample_2/archetypes_2"};
		ResourceExplorer explorer = new ResourceExplorer();
		explorer.explore(args[0]);
		System.out.println("Repositório: " + args[0]);
		explorer.projetos.forEach(System.out::println);
	}
}
