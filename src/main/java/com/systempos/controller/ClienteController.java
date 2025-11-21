package com.systempos.controller;

import com.systempos.model.Cliente;
import com.systempos.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Lista todos los clientes
     */
    @GetMapping
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.obtenerTodos();
        model.addAttribute("clientes", clientes);
        model.addAttribute("cliente", new Cliente());
        return "pages/clientes";
    }

    /**
     * Guardar nuevo cliente
     */
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente,
                                 RedirectAttributes redirectAttributes) {
        try {
            clienteService.guardar(cliente);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    /**
     * Editar cliente
     */
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Cliente> clientes = clienteService.obtenerTodos();

        model.addAttribute("cliente", cliente);
        model.addAttribute("clientes", clientes);
        model.addAttribute("editando", true);

        return "pages/clientes";
    }

    /**
     * Actualizar cliente
     */
    @PostMapping("/actualizar")
    public String actualizarCliente(@ModelAttribute Cliente cliente,
                                    RedirectAttributes redirectAttributes) {
        try {
            clienteService.guardar(cliente);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    /**
     * Eliminar cliente
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }
}