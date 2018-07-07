package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.ParseTree;
import jyscript.parsetree.nodes.IntNode;

public class ThermeNode extends IntNode {

	private FactorNode m_Factor;
	private ThermeDerivedNode m_Derived;

	public ThermeNode(FactorNode node, ThermeDerivedNode node2) {
		m_Factor = node;
		m_Derived = node2;
	}

	public ThermeNode(FactorNode node){
		this(node, new ThermeDerivedNode());
	}

	public Integer eval() {
		return m_Derived.eval(m_Factor.eval());
	}

	public Integer eval(int a) {
		return m_Derived.eval(a);
	}
}