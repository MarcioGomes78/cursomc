package com.mjgomes.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.ok;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mjgomes.cursomc.domain.Cidade;
import com.mjgomes.cursomc.domain.Estado;
import com.mjgomes.cursomc.dto.CidadeDTO;
import com.mjgomes.cursomc.dto.EstadoDTO;
import com.mjgomes.cursomc.services.CidadeService;
import com.mjgomes.cursomc.services.EstadoService;

//anotação que indica que esta classe é um controlador REST
@RestController
//indica o caminho da requisição
@RequestMapping(value = "/estados")
public class EstadoResource {

    //anotação que indica que esta variável será injetada pelo Spring
    @Autowired
    private EstadoService service;

    @Autowired
    private CidadeService cidadeService;

    //anotação mais moderna e recomendada
    @RequestMapping(method=RequestMethod.GET)
    //Retorna uma lista de DTOs de estados
    public ResponseEntity<List<EstadoDTO>> findAll() {
        //Lista de estados
        List<Estado> list = service.findAll();
        //Transforma a lista de estados em uma lista de DTOs
        List<EstadoDTO> listDto = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
        // Retorna a lista de DTOs de estados
        return ok().body(listDto);
    }

    @RequestMapping(value = "/{estadoId}/cidades", method = RequestMethod.GET)
    public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {
        List<Cidade> list = cidadeService.findByEstado(estadoId);
        List<CidadeDTO> listDto = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
