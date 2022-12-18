package com.heoj10272.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.heoj10272.dto.BoardDTO;
import com.heoj10272.dto.PageRequestDTO;
import com.heoj10272.security.dto.AuthMemberDTO;
import com.heoj10272.service.BoardService;

import java.security.Principal;

@Controller
@RequestMapping("/board/")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public void list(@AuthenticationPrincipal AuthMemberDTO authMember, PageRequestDTO pageRequestDTO, Model model){

        log.info("list............." + pageRequestDTO);

        model.addAttribute("result", boardService.getList(pageRequestDTO));

        log.info("member..................");
        log.info(authMember);
    }


    @GetMapping("/register")
    public void register(Principal principal, Model model){

        log.info("regiser get...");
        model.addAttribute("email", principal.getName());
    }


    @PostMapping("/register")
    public String registerPost(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        log.info("boardDTO..." + boardDTO);
        //새로 추가된 엔티티의 번호
        Long bno = boardService.register(boardDTO);

        log.info("BNO: " + bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify" })
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model, Principal principal){

        log.info("bno: " + bno);

        BoardDTO boardDTO = boardService.getBoard(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
        if (principal != null) {
            model.addAttribute("email", principal.getName());
        }

    }


    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes redirectAttributes){


        log.info("bno: " + bno);

        boardService.removeWithReplies(bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";

    }

    @PostMapping("/modify")
    public String modify(BoardDTO dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes){


        log.info("post modify.........................................");
        log.info("dto: " + dto);

        boardService.modify(dto);

        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());

        redirectAttributes.addAttribute("bno",dto.getBno());

        return "redirect:/board/read";

    }
}
