package com.mentor.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.web.WebErrors;
import com.mentor.entity.Article;
import com.mentor.entity.NormalUser;
import com.mentor.entity.Tree;
import com.mentor.entity.UnifiedUser;
import com.mentor.manager.ArticleMng;
import com.mentor.manager.NormalUserMng;
import com.mentor.manager.TreeMng;

@Controller
public class MemberAct {
	
	@RequestMapping(value="/member/relationship.html")
	public String relationship(HttpServletRequest request, ModelMap model, UnifiedUser user){
		
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		
		Tree tree = treeMng.getByUsername(normalUser.getId()+"");
		Integer leftNum = tree.getLeftNum();
		Integer rightNum = tree.getRightNum();
		Integer rankNum = tree.getRankNum();
		
		model.addAttribute("id", normalUser.getId());
		model.addAttribute("senior_list", getNormalUserListByTree(treeMng.getFathersByRank(leftNum, rightNum, rankNum, 3)));
		model.addAttribute("junior_1_list", getNormalUserListByTree(treeMng.getChildrenByRank(leftNum, rightNum, rankNum, 1)));
		model.addAttribute("junior_2_list", getNormalUserListByTree(treeMng.getChildrenByRank(leftNum, rightNum, rankNum, 2)));
		model.addAttribute("junior_3_num", treeMng.getChildrenNumByRank(leftNum, rightNum, rankNum, 3));
		
		return "/member/relationship";
	}
	
	@RequestMapping(value="/member/relationship_details.html")
	public String relationshipDetails(HttpServletRequest request, ModelMap model, UnifiedUser user, Integer rank){
		
		WebErrors errors = validateRelationshipDetails(request, user, rank);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		
		Tree tree = treeMng.getByUsername(normalUser.getId()+"");
		Integer leftNum = tree.getLeftNum();
		Integer rightNum = tree.getRightNum();
		Integer rankNum = tree.getRankNum();
		
		model.addAttribute("id", normalUser.getId());
		model.addAttribute("rank", rank);
		
		if (rank==0) {
			model.addAttribute("list", getNormalUserListByTree(treeMng.getFathersByRank(leftNum, rightNum, rankNum, 10)));
		} else {
			model.addAttribute("list", getNormalUserListByTree(treeMng.getChildrenByRank(leftNum, rightNum, rankNum, rank)));
		}
		
		return "/member/relationship_details";
	}
	
	private List<NormalUser> getNormalUserListByTree(List<Tree> treeList) {
		List<NormalUser> n1 = new ArrayList<NormalUser>();
		List<NormalUser> n2 = new ArrayList<NormalUser>();
		for (Tree tree : treeList) {
			NormalUser normalUser = normalUserMng.getByExternalNo(Long.valueOf(tree.getUsername()));
			if (normalUser.getUsername()==null)
				n2.add(normalUser);
			else
				n1.add(normalUser);
		}
		n1.addAll(n2);
		return n1;
	}
	
	@RequestMapping(value="/member/homepage.html")
	public String homepage(HttpServletRequest request, ModelMap model, UnifiedUser user){
		
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model,
					"/common/error_message");
		}
		
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		
		List<Article> articleList = articleMng.getByUser(user.getId());
		
		model.addAttribute("normalUser", normalUser);
		model.addAttribute("articleList", articleList);
		
		return "/member/homepage";
	}
	
	private WebErrors validateRelationshipDetails(HttpServletRequest request, UnifiedUser user, Integer rank) {
		WebErrors errors = validate(request, user);
		if (errors.hasErrors()) {
			return errors;
		}
		if (rank==null) {
			errors.addError("级数不存在");
		}
		if (rank<0 || rank>10) {
			errors.addError("级数越级");
		}
		return errors;
	}
	
	private WebErrors validate(HttpServletRequest request, UnifiedUser user) {
		WebErrors errors = WebErrors.create(request);
		if (user==null) {
			errors.addError("基本用户不存在");
			return errors;
		}
		NormalUser normalUser = normalUserMng.getByExternalNo(user.getId());
		if (normalUser==null) {
			errors.addError("用户不存在");
		}
		return errors;
	}
	
	@Autowired
	private ArticleMng articleMng;
	@Autowired
	private TreeMng treeMng;
	@Autowired
	private NormalUserMng normalUserMng;
	
}
