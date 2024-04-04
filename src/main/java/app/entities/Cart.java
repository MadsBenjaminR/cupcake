package app.entities;


import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartLine> cartLines=new ArrayList<>();

    public void add(Top top, Bottom bottom, int quantity){

        CartLine cartLine=new CartLine(top,bottom,quantity);
        cartLines.add(cartLine);

    }

    public int getTotal(){
       int sum=0;

        for (CartLine cartLine : cartLines) {
            sum += (cartLine.getTop().getPrice() + cartLine.getBottom().getPrice()) * cartLine.getQuantity();

        }
        return sum;
    }
    public int getTotalCartLines(){
     return cartLines.size();

    }

}
