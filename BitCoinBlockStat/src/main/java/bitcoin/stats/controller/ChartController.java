package bitcoin.stats.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChartController {

    @Value("${request.blocks.default.number}")
    private int defaultBlocksNumber;

    @RequestMapping("/chart")
    public String chart(@RequestParam(value = "size", required = false) Integer size, Model model) {
        Integer blockSize = size != null ? size : defaultBlocksNumber;
        model.addAttribute("size", blockSize);
        return "chart";
    }
}
