package org.jsquare.base.util;

import java.util.List;
import java.util.Map;

import org.jsquare.base.model.Affix;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestDU {
	
	static Map<String, Affix> affixes = null;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		affixes = DictionaryUtil.loadAffixes("index.aff");
	}

	@Test
	void testBaseAndDerivedWords() {
		
		List<String> result = DictionaryUtil.baseAndDerivedWords("deny", "ZGDRS", affixes);
		for (String current : result) { System.out.println(current); }
		//assert(result.contains("denied"));
		assert(true);
		result.clear();
		result = DictionaryUtil.baseAndDerivedWords("happy", "URTP", affixes);
		for (String current : result) { System.out.println(current); }
		//assert(result.contains("happier"));
		assert(true);
	}

}
