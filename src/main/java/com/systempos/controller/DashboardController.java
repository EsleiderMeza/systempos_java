//package com.systempos.controller;
//
//import com.systempos.service.DashboardService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class DashboardController {
//
//    @Autowired
//    private DashboardService dashboardService;
//
//    @GetMapping({"/", "/dashboard"})
//    public String dashboard(Model model) {
//        model.addAttribute("usuario", "Admin");  // Usuario fijo temporal
//        model.addAttribute("ventasHoy", dashboardService.getVentasHoy());
//        model.addAttribute("totalProductos", dashboardService.getTotalProductos());
//        model.addAttribute("totalClientes", dashboardService.getTotalClientes());
//        model.addAttribute("totalProveedores", dashboardService.getTotalProveedores());
//        model.addAttribute("totalVentasHoy", dashboardService.getTotalVentasHoy());
//
//        return "dashboard";
//    }
//}

package com.systempos.controller;

import com.systempos.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model, Authentication auth) {
        // Obtener el nombre de usuario autenticado
        String username = auth != null ? auth.getName() : "Invitado";

        model.addAttribute("usuario", username);
        model.addAttribute("ventasHoy", dashboardService.getVentasHoy());
        model.addAttribute("totalProductos", dashboardService.getTotalProductos());
        model.addAttribute("totalClientes", dashboardService.getTotalClientes());
        model.addAttribute("totalProveedores", dashboardService.getTotalProveedores());
        model.addAttribute("totalVentasHoy", dashboardService.getTotalVentasHoy());

        return "dashboard";
    }
}