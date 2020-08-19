package spyclass;

public class Classe {
	private static final String NAO = "Não";
	private static final String SIM = "Sim";
	private static final String COLUNM_SEPARATOR = "'; '";
	private static final String ASPAS_SIMPLES = "'";
	private String className;
	private String classPath;
	private Boolean classRepresentative;
	private Boolean name, firstName, lastName, fullName, nickName, nick, password, pwd, sex, gender, age, birthday, bday, weight, height, race, skinColor;
	private String atributos;
	

	public Classe(String className, String classPath, Boolean classRepresentative) {
		this.className = className;
		this.classPath = classPath;
		this.classRepresentative = classRepresentative;
	}

	public Classe(String className, String classPath, Boolean classRepresentative, Boolean name, Boolean firstName,
			Boolean lastName, Boolean fullName, Boolean nickName, Boolean nick, Boolean password, Boolean pwd,
			Boolean sex, Boolean gender, Boolean age, Boolean birthday, Boolean bday, Boolean weight, Boolean height,
			Boolean race, Boolean skinColor, String atributos) {
		this.className = className;
		this.classPath = classPath;
		this.classRepresentative = classRepresentative;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.nickName = nickName;
		this.nick = nick;
		this.password = password;
		this.pwd = pwd;
		this.sex = sex;
		this.gender = gender;
		this.age = age;
		this.birthday = birthday;
		this.bday = bday;
		this.weight = weight;
		this.height = height;
		this.race = race;
		this.skinColor = skinColor;
		this.atributos = atributos;
	}



	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public Boolean getClassRepresentative() {
		return classRepresentative;
	}

	public void setClassRepresentative(Boolean classRepresentative) {
		this.classRepresentative = classRepresentative;
	}

	
	
	public Boolean getName() {
		return name;
	}

	public void setName(Boolean name) {
		this.name = name;
	}

	public Boolean getFirstName() {
		return firstName;
	}

	public void setFirstName(Boolean firstName) {
		this.firstName = firstName;
	}

	public Boolean getLastName() {
		return lastName;
	}

	public void setLastName(Boolean lastName) {
		this.lastName = lastName;
	}

	public Boolean getFullName() {
		return fullName;
	}

	public void setFullName(Boolean fullName) {
		this.fullName = fullName;
	}

	public Boolean getNickName() {
		return nickName;
	}

	public void setNickName(Boolean nickName) {
		this.nickName = nickName;
	}

	public Boolean getNick() {
		return nick;
	}

	public void setNick(Boolean nick) {
		this.nick = nick;
	}

	public Boolean getPassword() {
		return password;
	}

	public void setPassword(Boolean password) {
		this.password = password;
	}

	public Boolean getPwd() {
		return pwd;
	}

	public void setPwd(Boolean pwd) {
		this.pwd = pwd;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public Boolean getAge() {
		return age;
	}

	public void setAge(Boolean age) {
		this.age = age;
	}

	public Boolean getBirthday() {
		return birthday;
	}

	public void setBirthday(Boolean birthday) {
		this.birthday = birthday;
	}

	public Boolean getBday() {
		return bday;
	}

	public void setBday(Boolean bday) {
		this.bday = bday;
	}

	public Boolean getWeight() {
		return weight;
	}

	public void setWeight(Boolean weight) {
		this.weight = weight;
	}

	public Boolean getHeight() {
		return height;
	}

	public void setHeight(Boolean height) {
		this.height = height;
	}

	public Boolean getRace() {
		return race;
	}

	public void setRace(Boolean race) {
		this.race = race;
	}

	public Boolean getSkinColor() {
		return skinColor;
	}

	public void setSkinColor(Boolean skinColor) {
		this.skinColor = skinColor;
	}
	
	public String getAtributos() {
		return atributos;
	}
	
	public void setAtributos(String atributos) {
		this.atributos = atributos;
	}

	@Override
	public String toString() {
		return ASPAS_SIMPLES + className + COLUNM_SEPARATOR + classPath + COLUNM_SEPARATOR
				+ (classRepresentative ? SIM : NAO) + ASPAS_SIMPLES;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + className.hashCode();
		hash = 31 * hash + (classPath == null ? 0 : classPath.hashCode());
		hash = 31 * hash + (classRepresentative == null ? 0 : classRepresentative.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Classe classe = (Classe) obj;
		return className.equals(classe.className)
				&& (classPath.equals(classe.classPath) && classRepresentative.equals(classe.classRepresentative));
	}
}
