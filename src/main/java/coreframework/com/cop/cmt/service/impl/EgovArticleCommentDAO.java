package coreframework.com.cop.cmt.service.impl;

import java.util.List;

import coreframework.com.cop.cmt.service.Comment;
import coreframework.com.cop.cmt.service.CommentVO;
import org.springframework.stereotype.Repository;

import coreframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("EgovArticleCommentDAO")
public class EgovArticleCommentDAO extends EgovComAbstractDAO{

	public List<?> selectArticleCommentList(CommentVO commentVO) {
		return list("ArticleComment.selectArticleCommentList", commentVO);
	}

	public int selectArticleCommentListCnt(CommentVO commentVO) {
		return (Integer)selectOne("ArticleComment.selectArticleCommentListCnt", commentVO);
	}

	public void insertArticleComment(Comment comment) {
		insert("ArticleComment.insertArticleComment", comment);
	}

	public void deleteArticleComment(CommentVO commentVO) {
		update("ArticleComment.deleteArticleComment", commentVO);
	}

	public CommentVO selectArticleCommentDetail(CommentVO commentVO) {
		return (CommentVO) selectOne("ArticleComment.selectArticleCommentDetail", commentVO);
	}

	public void updateArticleComment(Comment comment) {
		update("ArticleComment.updateArticleComment", comment);
	}

}
