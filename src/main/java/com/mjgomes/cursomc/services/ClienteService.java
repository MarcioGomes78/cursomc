package com.mjgomes.cursomc.services;

import com.mjgomes.cursomc.domain.Cidade;
import com.mjgomes.cursomc.domain.Cliente;
import com.mjgomes.cursomc.domain.Endereco;
import com.mjgomes.cursomc.dto.ClienteDTO;
import com.mjgomes.cursomc.dto.ClienteNewDTO;
import com.mjgomes.cursomc.enums.TipoCliente;
import com.mjgomes.cursomc.repositories.ClienteRepository;
import com.mjgomes.cursomc.repositories.EnderecoRepository;
import com.mjgomes.cursomc.services.exceptions.DataIntegrityException;
import com.mjgomes.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Regras de negócio de Cliente: CRUD, conversão DTO <-> entidade e criptografia de senha no cadastro.
@Service
public class ClienteService {

    @Autowired
    private BCryptPasswordEncoder pe;

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Cliente find(Integer id) {

        Optional<Cliente> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found! Id: " + id + ", Type: " + Cliente.class.getName()));
    }

    // Salva o cliente primeiro para gerar o id (necessário como FK em Endereco), depois salva os
    // endereços associados; o segundo repo.save(obj) apenas devolve o cliente já com essas associações.
    public Cliente insert(Cliente obj) {
        obj.setId(null);
        obj = repo.save(obj);
        enderecoRepository.saveAll(obj.getEnderecos());
        return repo.save(obj);
    }

    // quando o id não é null ele atualiza o objeto.
    public Cliente update(Cliente obj) {
        Cliente newObj = find(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }

    // find(id) garante 404 se o id não existir; a FK de Pedido vira DataIntegrityViolationException,
    // traduzida para uma mensagem amigável (não dá pra apagar cliente com pedidos).
    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir  porque há pedidos relacionados.");
        }
        repo.deleteById(id);
    }

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        // PageRequest é uma classe do Spring Data que implementa a interface Pageable,
        // e que prepara e ar mazena as informações de paginação para serem enviadas ao repositório.
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }

    // Usado na atualização (PUT): não altera cpfOuCnpj/tipo/senha, só nome/email (ver updateData).
    public Cliente fromDTO(ClienteDTO objDto) {
        return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
    }

    // A senha deve ser criptografada antes de salvar o cliente no banco de dados. usando o BCryptPasswordEncoder
    public Cliente fromDTO(ClienteNewDTO objDto) {
        Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
        Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
        Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
        cli.getEnderecos().add(end);
        cli.getTelefones().add(objDto.getTelefone1());
        if (objDto.getTelefone2() != null) {
            cli.getTelefones().add(objDto.getTelefone2());
        }
        if (objDto.getTelefone3() != null) {
            cli.getTelefones().add(objDto.getTelefone3());
        }
        return cli;
    }

    private  void updateData(Cliente newObj, Cliente objDto) {
        newObj.setNome(objDto.getNome());
        newObj.setEmail(objDto.getEmail());
    }
}
