package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addform";
    }




    // @PostMapping("/add")
    public String saveV1(@ModelAttribute("item") Item item/*, Model model*/) {
        itemRepository.save(item);
        // model.addAttribute("item", item);    // 자동 추가 -> 생략 가능
        return "basic/item";
    }

    //@PostMapping("/add")
    public String saveV2(@ModelAttribute Item item) {
        itemRepository.save(item);  // Item -> item 변수명으로 ModelAttribute 생성
        return "basic/item";
    }

    //@PostMapping("/add")
    public String saveV3(Item item) {
        itemRepository.save(item);  // 마찬가지로 생략시 Item -> item 변수명으로 ModelAttribute 생성
        return "basic/item";
    }

    //@PostMapping("/add")
    public String saveV4(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId(); // PRG post값은 redirect로 다른 Get으로 보내줘야
    }

    @PostMapping("/add")
    public String saveV5(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);    // 쿼리파라미터 형식으로 들어감
        return "redirect:/basic/items/{itemId}";
    }






    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editform";
    }

    //@PostMapping("/{itemId}/edit")
    public String editV1(@PathVariable Long itemId, String itemName, Integer price, Integer quantity, Model model) {
        itemRepository.update(itemId, new Item(itemName, price, quantity));
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item); // 저장 후 목록 보면 따로 id포함한 item을 안넣어도 됨
        return "basic/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
