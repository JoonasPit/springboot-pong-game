package game.project.course.controller;
import java.io.Console;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import game.project.course.domain.Comment;
import game.project.course.domain.CommentRepository;
import game.project.course.domain.Score;
import game.project.course.domain.ScoreRepository;
@Controller
public class PageController {
	
	@Autowired
	private ScoreRepository scoreRepo;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@RequestMapping(value="/")
	public String sayHe() {
		return "index";
	}
	@RequestMapping(value="/game")
	public String gameTetris(Model model) {
		model.addAttribute("score", new Score());
		return "game";
	}
	@RequestMapping(value="/leaderboard", method = RequestMethod.GET)
	public String getLeaderboard(Model model) {
		model.addAttribute("score", scoreRepo.findAll());
		return "leaderboard";
	}	
	//REST
	@RequestMapping(value ="/scores", method = RequestMethod.GET)
	public @ResponseBody List<Score> scoreRest(){
		return (List<Score>) scoreRepo.findAll();
	}
	
	@RequestMapping(value= "/login")
	public String userLogin(){
		return "login";
	}
	@GetMapping(value= "/comments")
	public String getCommentPage(Model model) {
		model.addAttribute("comments",commentRepository.findAll());
		return "comments";
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value="/delete/{id}")
	public String commentDelete(@PathVariable("id") Long id, Model model) {
		commentRepository.deleteById(id);
		return "redirect:/comments";
	}
	@PostMapping(value = "/submitscore")
	public String submitScore(Authentication authentication, @RequestParam("runscore") int score) {
		String username = authentication.getName();
		Score currentScore = new Score(username, score);
		scoreRepo.save(currentScore);
		return "redirect:/leaderboard";
	}
	@PostMapping(value = "/postcomment")
	public String postComment(Authentication authentication, @RequestParam("usercomment") String comment) {
		String username = authentication.getName();
		Comment thisComment = new Comment(comment,username);
		commentRepository.save(thisComment);
		return "redirect:/comments";
	}
}
