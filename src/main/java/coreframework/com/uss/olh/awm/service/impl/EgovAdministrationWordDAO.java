package coreframework.com.uss.olh.awm.service.impl;

import java.util.List;

import coreframework.com.uss.olh.awm.service.AdministrationWordVO;
import org.springframework.stereotype.Repository;

import coreframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("EgovAdministrationWordDAO")
public class EgovAdministrationWordDAO extends EgovComAbstractDAO {

	public List<?> selectAdministrationWordList(AdministrationWordVO searchVO) {
		return list("AdministrationWord.selectAdministrationWordList", searchVO);
	}

	public int selectAdministrationWordListCnt(AdministrationWordVO searchVO) {
		return (Integer) selectOne("AdministrationWord.selectAdministrationWordListCnt", searchVO);
	}

	public AdministrationWordVO selectAdministrationWordDetail(AdministrationWordVO administrationWord) {
		return (AdministrationWordVO) selectOne("AdministrationWord.selectAdministrationWordDetail", administrationWord);
	}

	public void insertAdministrationWord(AdministrationWordVO administrationWordVO) {
		insert("AdministrationWord.insertAdministrationWord", administrationWordVO);
	}

	public void updateAdministrationWord(AdministrationWordVO administrationWordVO) {
		update("AdministrationWord.updateAdministrationWord", administrationWordVO);
	}

	public void deleteAdministrationWord(AdministrationWordVO administrationWordVO) {
		delete("AdministrationWord.deleteAdministrationWord", administrationWordVO);
	}

}
