package vladimir.java_dz_14;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shops")
public class ShopController {
    private final List<Shop> shops = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ShopController() {
        shops.add(new Shop(idGenerator.getAndIncrement(), "FixPrice", "Lenina 15", "555-0101", "fix@shop.ru", "fixprice.ru", "Хозяйственный", "Все по одной цене."));
        shops.add(new Shop(idGenerator.getAndIncrement(), "SportMaster", "Mira 42", "555-0202", "sport@shop.ru", "sportmaster.ru", "Спортивный", "Товары для спорта и отдыха."));
    }

    @GetMapping
    public String listShops(@RequestParam(value = "search", required = false) String search, Model model) {
        if (search != null && !search.trim().isEmpty()) {
            String lowerSearch = search.toLowerCase();
            List<Shop> filtered = shops.stream()
                    .filter(s -> s.getName().toLowerCase().contains(lowerSearch)
                            || s.getCategory().toLowerCase().contains(lowerSearch)
                            || s.getAddress().toLowerCase().contains(lowerSearch))
                    .collect(Collectors.toList());
            model.addAttribute("shops", filtered);
        } else {
            model.addAttribute("shops", shops);
        }
        model.addAttribute("search", search);
        return "list";
    }

    @GetMapping("/{id}")
    public String viewShop(@PathVariable Long id, Model model) {
        Shop shop = shops.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
        model.addAttribute("shop", shop);
        return "view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("shop", new Shop());
        return "form";
    }

    @PostMapping("/save")
    public String saveShop(@ModelAttribute Shop shop) {
        if (shop.getId() == null) {
            shop.setId(idGenerator.getAndIncrement());
            shops.add(shop);
        } else {
            for (int i = 0; i < shops.size(); i++) {
                if (shops.get(i).getId().equals(shop.getId())) {
                    shops.set(i, shop);
                    break;
                }
            }
        }
        return "redirect:/shops";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Shop shop = shops.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
        model.addAttribute("shop", shop);
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String deleteShop(@PathVariable Long id) {
        shops.removeIf(s -> s.getId().equals(id));
        return "redirect:/shops";
    }
}
