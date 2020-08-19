# BiFroS<T> Project

<pre>
O BiFroS<T> é um projeto para identificação de viés em softwares.
</pre>

## Description
A questão do viés em software tem se tornado cada vez mais importante, especialmente na atualidade com o amplo uso de Machine Learning na qual cada vez mais algoritmos tem substituído pessoas na tomada de decisões. Diversos estudos tem tratado da questão e abordado tanto os fatores causadores como também suas consequências. 
A concepção desta ferramenta está embasada no fenômeno do viés induzido pela ordenação de lista de pesssoas [Sullivan et al, 2017] e também na seleção dos top-k elementos de uma lista [Zehlike, 2017]. 

A mesma tem como propósito identificar quais são as classes pessoas de um projeto, e uma vez identificadas, verificar onde ocorrem no código ordenações de listas dessas classes, ou seja, ordenações de listas de pessoas o que incorre nos dois casos de viés: ordenação e top-k elementos.

Para executar a ferramenta basta seguir os passos da documentação do exemplo:
![/example/README.md](/example/README.md)

## References 
ZEHLIKE, Meike et al. Fa* ir: A fair top-k ranking algorithm. In: Proceedings of the 2017 ACM on Conference on Information and Knowledge Management. 2017. p. 1569-1578. 
SULLIVAN, D.; CAMINHA, C.; DANTAS, V.; FURTADO, E.; FURTADO, V.; ALMEIDA, V.Understanding the impact of the alphabetical ordering of names in user interfaces: a gender biasanalysis.arXiv:2007.04361, 2020.
