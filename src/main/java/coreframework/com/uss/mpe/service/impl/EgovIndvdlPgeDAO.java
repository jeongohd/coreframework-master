package coreframework.com.uss.mpe.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import coreframework.com.cmm.service.impl.EgovComAbstractDAO;
import coreframework.com.uss.mpe.service.IndvdlPgeVO;

@Repository("EgovIndvdlPgeDAO")
public class EgovIndvdlPgeDAO extends EgovComAbstractDAO {

	public List<?> selectIndvdlPgeList(IndvdlPgeVO searchVO) {
		return selectList("IndvdlPge.selectIndvdlPgeList", searchVO);
	}

	public int selectIndvdlPgeListCnt(IndvdlPgeVO searchVO) {
		return (Integer) selectOne("IndvdlPge.selectIndvdlPgeListCnt", searchVO);
	}

	public IndvdlPgeVO selectIndvdlPgeDetail(IndvdlPgeVO indvdlPgeVO) {
		return (IndvdlPgeVO) selectOne("IndvdlPge.selectIndvdlPgeDetail", indvdlPgeVO);
	}

	public void insertIndvdlPge(IndvdlPgeVO indvdlPgeVO) {
		insert("IndvdlPge.insertIndvdlPge", indvdlPgeVO);
	}

	public void updateIndvdlPge(IndvdlPgeVO indvdlPgeVO) {
		update("IndvdlPge.updateIndvdlPge", indvdlPgeVO);
	}

}
