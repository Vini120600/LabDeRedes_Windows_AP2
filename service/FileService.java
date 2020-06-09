package org.comeia.project.service;

import java.io.File;
import java.io.IOException;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FileService {
	
		{try {
			File meuArquivo = new File("fileTeste.txt");
			if(meuArquivo.createNewFile()) {
				System.out.println("ARQUIVO CRIADO: " + meuArquivo.getName());
			}
			else {
				System.out.println("ARQUIVO EXISTENTE.");
			}
		}
		catch(IOException e) {
			System.out.println("...ERRO NA CRIAÇÃO DO ARQUIVO");
			e.printStackTrace();
		}
	}
}	
	
