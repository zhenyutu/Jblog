package cn.tzy.Jblog.service;

import cn.tzy.Jblog.dao.CommentDao;
import cn.tzy.Jblog.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by tuzhenyu on 17-7-23.
 * @author tuzhenyu
 */
@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private SensitiveService sensitiveService;

    public void addCommet(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        commentDao.insertComment(comment);
    }

    public List<Comment> getCommentsByArticleId(int articleId){
        return commentDao.selectCommentsByArticleId(articleId);
    }

    public int getCommentsCount(int articleId){
        return commentDao.getCommentCountByArticleId(articleId);
    }

    public void deleteComment(int commentId){
        commentDao.updateStatus(commentId,1);
    }

    public Comment getCommentById(int commentId){
        return commentDao.seletById(commentId);
    }
}
