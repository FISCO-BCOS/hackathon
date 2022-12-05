package org.prepay.prepay.controller;

import org.prepay.prepay.model.CommonResponse;
import org.prepay.prepay.service.Imple.CardService;
import org.prepay.prepay.vo.CardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/card")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping("/create")
    @ResponseBody
    public CommonResponse cardRegistry(@RequestParam("userId") String userId,
                                       @RequestParam("shopId") String shopId,
                                       @RequestParam("balance") Integer balance,
                                       @RequestParam("text") String text,
                                       HttpServletResponse response) throws Exception {
        String data = cardService.createCard(userId, shopId, balance, text);
        CommonResponse res = new CommonResponse();
        int code = data != null ? 200 : 500;
        String msg = data != null ? "ok" : "error";
        res.setCode(String.valueOf(code));
        res.setMessage(msg);
        res.setData(data);
        return res;
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Object> cardList() throws Exception {
        return cardService.cardList();
    }

    @GetMapping("/one")
    @ResponseBody
    public CardVo cardOne(String cardId) throws Exception {
        return cardService.getCardById(cardId);
    }

    @GetMapping("/delete")
    @ResponseBody
    public boolean deleteOne(String cardId) throws Exception {
        return cardService.deleteById(cardId);
    }


}
