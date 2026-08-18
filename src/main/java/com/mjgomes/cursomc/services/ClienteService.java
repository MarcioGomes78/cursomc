package com.mjgomes.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mjgomes.cursomc.domain.Cidade;
import com.mjgomes.cursomc.domain.Cliente;
import com.mjgomes.cursomc.domain.Endereco;
import com.mjgomes.cursomc.dto.ClienteDTO;
import com.mjgomes.cursomc.dto.ClienteNewDTO;
import com.mjgomes.cursomc.enums.Perfil;
import com.mjgomes.cursomc.enums.TipoCliente;
import com.mjgomes.cursomc.repositories.ClienteRepository;
import com.mjgomes.cursomc.repositories.EnderecoRepository;
import com.mjgomes.cursomc.security.UserSS;
import com.mjgomes.cursomc.services.exceptions.AuthorizationException;
import com.mjgomes.cursomc.services.exceptions.DataIntegrityException;
import com.mjgomes.cursomc.services.exceptions.ObjectNotFoundException;

// Regras de negócio de Cliente: CRUD, conversão DTO <-> entidade e criptografia de senha no cadastro.
@Service
public class ClienteService {

    @Autowired
    private BCryptPasswordEncoder pe;

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ImageService imageService;

    @Value("${img.prefix.client.profile}")
    private String prefix;

    @Value("${img.profile.size}")
    private Integer size;

    public Cliente find(Integer id) {

        // Só pode ver o próprio cadastro ou, se for ADMIN, o de qualquer cliente; caso contrário, 403.
        UserSS user = UserService.authenticated();
        if (user == null || !user.getId().equals(id) || !user.hasRole(Perfil.ADMIN)) {
            throw new AuthorizationException("Acesso negado");
        }

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

        // Se o telefone 2 ou 3 for preenchido, adicionar ao cliente
        if (objDto.getTelefone2() != null) {
            cli.getTelefones().add(objDto.getTelefone2());
        }
        if (objDto.getTelefone3() != null) {
            cli.getTelefones().add(objDto.getTelefone3());
        }
        return cli;
    }

    // Método auxiliar para atualizar os dados do cliente.
    private  void updateData(Cliente newObj, Cliente objDto) {
        newObj.setNome(objDto.getNome());
        newObj.setEmail(objDto.getEmail());
    }

    // Método para fazer upload da foto de perfil do cliente.
    public URI uploadProfilePicture(MultipartFile multipartFile){

        // Busca o cliente logado
        UserSS user = UserService.authenticated();
        if (user == null) {
            throw new AuthorizationException("Acesso negado");
        }

        // Transforma a imagem para JPG
        BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);

        // Cortar a imagem para que ela fique quadrada
        jpgImage = imageService.cropSquare(jpgImage);

        // Redimensionar a imagem para que ela tenha o tamanho especificado
        jpgImage = imageService.resize(jpgImage, size);

        // Nome do arquivo
        String fileName = prefix + user.getId() + ".jpg";
        
        //faz o upload da imagem
        return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
    }
}