package coreframework.com.uss.ion.nws.service.impl;

import java.util.List;

import coreframework.com.uss.ion.nws.service.NewsVO;
import org.springframework.stereotype.Repository;

import coreframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("EgovNewsDAO")
public class EgovNewsDAO extends EgovComAbstractDAO {

	public List<?> selectNewsList(NewsVO searchVO) {
		return list("NewsManage.selectNewsList", searchVO);
	}

	public int selectNewsListCnt(NewsVO searchVO) {
		return (Integer) selectOne("NewsManage.selectNewsListCnt", searchVO);
	}

	public void insertNews(NewsVO newsVO) {
		insert("NewsManage.insertNews", newsVO);
	}

	public NewsVO selectNewsDetail(NewsVO newsVO) {
		return (NewsVO) selectOne("NewsManage.selectNewsDetail", newsVO);
	}

	public void updateNews(NewsVO newsVO) {
		update("NewsManage.updateNews", newsVO);
	}

	public void deleteNews(NewsVO newsVO) {
		delete("NewsManage.deleteNews", newsVO);
	}

}
