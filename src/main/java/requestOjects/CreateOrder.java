package requestOjects;

import java.util.List;

public class CreateOrder {

    private String[] ingredients;

    public CreateOrder(List<String> ingredients){
        this.ingredients = ingredients.toArray(new String[0]);
    }

    public CreateOrder(){
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }


}
