package com.codex.aposta.service;

import com.codex.aposta.model.Aposta;
import com.codex.aposta.model.Apostador;
import com.codex.aposta.model.dto.ApostaIn;
import com.codex.aposta.model.dto.ApostaOut;
import com.codex.aposta.model.dto.ApostasOut;
import com.codex.aposta.repository.ApostaRepository;
import com.codex.aposta.repository.ApostadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ApostaService {

    private final ApostadorRepository apostadorRepository;
    private final ApostaRepository apostaRepository;

    public ApostaOut salvaAposta(ApostaIn apostaIn) {
        ApostaOut apostaOut = null;

        try {
            Optional<Apostador> apostador = apostadorRepository.findById(apostaIn.getIdApostador());
            String numAposta = UUID.randomUUID().toString();
            Aposta aposta = new Aposta(numAposta, apostador.get());

            apostaRepository.save(aposta);
            apostaOut = new ApostaOut(numAposta, apostador.get().getNome(), apostador.get().getEmail());

        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(NOT_FOUND);
        }

        return apostaOut;
    }

    public List<ApostasOut> buscaApostasPorIdApostador(Long idApostador) {
        List<Aposta> apostaList = apostaRepository.findByIdApostador(idApostador);
        List<ApostasOut> list = new ArrayList<>();

        apostaList.forEach(apostas -> {
            ApostasOut apostasOut = new ApostasOut();
            apostasOut.setIdApostador(apostas.getApostador().getId());
            apostasOut.setNumeroAposta(apostas.getNumeroAposta());
            list.add(apostasOut);
        });

        return list;
    }
}
