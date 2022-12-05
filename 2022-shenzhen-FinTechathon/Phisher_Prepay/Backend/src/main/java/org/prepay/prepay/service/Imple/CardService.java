package org.prepay.prepay.service.Imple;

import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.prepay.prepay.model.bo.PrepaidCardCreateCardInputBO;
import org.prepay.prepay.model.bo.PrepaidCardDeleteCardInputBO;
import org.prepay.prepay.model.bo.PrepaidCardSelectByCardIDInputBO;
import org.prepay.prepay.service.PrepaidCardService;
import org.prepay.prepay.utils.UUIDUtil;
import org.prepay.prepay.vo.CardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.util.List;

@Service
public class CardService {
    @Autowired
    private PrepaidCardService prepaidCardService;

    public String createCard(String userId, String shopId, int balance, String text) throws Exception {
        String cardId = UUIDUtil.getUUID();
        long time = System.nanoTime();
        int conHash = (userId + shopId + text).hashCode();
        TransactionResponse response = prepaidCardService.createCard(new PrepaidCardCreateCardInputBO(cardId, userId, shopId,
                BigInteger.valueOf(balance), BigInteger.valueOf(conHash), BigInteger.valueOf(time)));
        File file = new File(cardId);
        if (!file.exists()) {
            file.createNewFile();
        }
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(text);
        outputStream.flush();
        outputStream.close();
        return 1 == response.getReturnCode() ? cardId : null;
    }

    public List<Object> cardList() throws Exception {
        TransactionResponse response = prepaidCardService.selectAll();
        if (response != null)
            return response.getReturnObject();
        return null;
    }

    public CardVo getCardById(String cardId) throws Exception {
        PrepaidCardSelectByCardIDInputBO inputBO = new PrepaidCardSelectByCardIDInputBO(cardId);
        TransactionResponse response = prepaidCardService.selectByCardID(inputBO);
        List<Object> list = response.getReturnObject();
        CardVo cardVo = new CardVo();
        if (list == null || list.get(0) == null) {
            return null;
        }
        cardVo.setCardId((String) list.get(0));
        cardVo.setUserName((String) list.get(1));
        cardVo.setShopName((String) list.get(2));
        cardVo.setBalance((BigInteger) list.get(3));
        File file = new File(cardId);
        if (!file.exists()) {
            file.createNewFile();
        }
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
        String text = (String) inputStream.readObject();
        cardVo.setText(text);
        inputStream.close();
        return cardVo;
    }

    public boolean deleteById(String cardId) throws Exception {
        PrepaidCardSelectByCardIDInputBO inputBO = new PrepaidCardSelectByCardIDInputBO(cardId);
        TransactionResponse response = prepaidCardService.selectByCardID(inputBO);
        List<Object> list = response.getReturnObject();
        if (list != null) {
            response = prepaidCardService.deleteCard(new PrepaidCardDeleteCardInputBO(cardId));
            return response.getReturnCode() == 1;
        }
        return false;
    }

}
