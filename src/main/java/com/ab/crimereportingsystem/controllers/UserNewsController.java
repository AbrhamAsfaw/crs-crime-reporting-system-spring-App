package com.ab.crimereportingsystem.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ab.crimereportingsystem.domains.Comment;
import com.ab.crimereportingsystem.domains.User;
import com.ab.crimereportingsystem.services.CommentService;
import com.ab.crimereportingsystem.services.NewsService;

@Controller
@RequestMapping("user/comment")
public class UserNewsController {
	
	
	private NewsService newsService;
	private CommentService commentService;
	@Autowired
	public UserNewsController(NewsService newsService, CommentService commentService) {
		this.newsService = newsService;
		this.commentService = commentService;
	}
	@ModelAttribute(name="comment")
	public Comment comment(Model model) {
		return new Comment();
	}
	@GetMapping("/{id}")
	public String goToComment(@PathVariable("id") Long id, Model model) {
		model.addAttribute("news", newsService.getById(id));
		model.addAttribute("comments", commentService.getByNews(newsService.getById(id)));
		return "user_comment";
	}
	
	@PostMapping("{id}")
	public String postComment(@PathVariable("id") Long id, @Valid Comment comment, Errors errors, Model model,@AuthenticationPrincipal User user) {
		if (errors.hasErrors()) {
			model.addAttribute("news", newsService.getById(id));
			model.addAttribute("comments", commentService.getByNews(newsService.getById(id)));
			return "user_comment";
		}
		comment.setNews(newsService.getById(id));
		comment.setUser(user);
		Comment newComment = new Comment();
		newComment.setNews(newsService.getById(id));
		newComment.setUser(user);
		newComment.setContent(comment.getContent());
		commentService.save(newComment);
		return "redirect:/public/news/"+id;
		
	}
	
}
