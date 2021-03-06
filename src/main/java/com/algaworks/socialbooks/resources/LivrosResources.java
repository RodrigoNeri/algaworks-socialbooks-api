package com.algaworks.socialbooks.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.socialbooks.domain.Comentario;
import com.algaworks.socialbooks.domain.Livro;
import com.algaworks.socialbooks.resources.exceptions.SocialBooksBadRequestException;
import com.algaworks.socialbooks.service.LivrosService;
import com.algaworks.socialbooks.services.exceptions.AutorNaoEncontradoException;


@RestController
@RequestMapping("/livros")
public class LivrosResources extends AbstractResource {

  @Autowired
  private LivrosService livrosService;

  @CrossOrigin
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Livro>> listar() {
    return ResponseEntity.status(HttpStatus.OK).body(livrosService.listar());
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Void> salvar(@Valid @RequestBody Livro livro) {
    try {
      livro = livrosService.salvar(livro);

      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
          .buildAndExpand(livro.getId()).toUri();

      return ResponseEntity.created(uri).build();
    } catch (AutorNaoEncontradoException e) {
      throw new SocialBooksBadRequestException("O autor informado não existe.", e);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> buscar(@PathVariable(value = "id") final Long id) {
    Livro livro = livrosService.buscar(id);

    return ResponseEntity.status(HttpStatus.OK).cacheControl(getCacheControlMaxAge()).body(livro);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> remover(@PathVariable(value = "id") final Long id) {
    livrosService.remover(id);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<Void> atualizar(@RequestBody Livro livro,
      @PathVariable(value = "id") final Long id) {

    livro.setId(id);
    livrosService.atualizar(livro);

    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/{id}/comentarios", method = RequestMethod.POST)
  public ResponseEntity<Void> adicionarComentario(@PathVariable(value = "id") final Long livroId,
      @RequestBody Comentario comentario) {

    comentario.setUsuario(getAuthenticatedUser());
    livrosService.salvarComentario(livroId, comentario);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

    return ResponseEntity.created(uri).build();

  }

  @RequestMapping(value = "/{id}/comentarios", method = RequestMethod.GET)
  public ResponseEntity<List<Comentario>> listarComentarios(
      @PathVariable(value = "id") final Long livroId) {

    List<Comentario> listaComentarios = livrosService.listarComentarios(livroId);
    return ResponseEntity.status(HttpStatus.OK).body(listaComentarios);
  }

}
