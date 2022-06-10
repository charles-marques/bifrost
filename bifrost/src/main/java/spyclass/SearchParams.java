package spyclass;

import java.util.List;

public class SearchParams {
	private List<String> attributes;
	private Integer qtdAttributes;

	public SearchParams() {
		super();
		this.qtdAttributes = 0;
	}

	public SearchParams(List<String> attributes, Integer qtdAttributes) {
		this();
		this.attributes = attributes;
		this.qtdAttributes = (qtdAttributes != null) ? qtdAttributes : 0;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public Integer getQtdAttributes() {
		return qtdAttributes;
	}

	public void setQtdAttributes(Integer qtdAttributes) {
		this.qtdAttributes = qtdAttributes;
	}

}
