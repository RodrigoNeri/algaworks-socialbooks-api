package com.algaworks.socialbooks.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.socialbooks.domain.Livro;
import com.algaworks.socialbooks.repository.LivrosRepository;

@RestController
@RequestMapping("/livros")
public class LivrosResources {

  @Autowired
  private LivrosRepository livrosRepository;

  @RequestMapping(method = RequestMethod.GET)
  public List<Livro> listar() {
    return livrosRepository.findAll();
  }

  @RequestMapping(method = RequestMethod.POST)
  public void salvar(@RequestBody final Livro livro) {
    livrosRepository.save(livro);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Livro buscar(@PathVariable(value = "id") final Long id) {
    return livrosRepository.findOne(id);
  }

}
