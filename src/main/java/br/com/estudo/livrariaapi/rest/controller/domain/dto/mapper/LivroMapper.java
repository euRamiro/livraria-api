package br.com.estudo.livrariaapi.rest.controller.domain.dto.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import br.com.estudo.livrariaapi.persistence.entity.LivroEntity;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.LivroDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class LivroMapper {

	private ModelMapper modelMapper;

	public LivroDto toDto(LivroEntity livroEntity) {		
		return modelMapper.map(livroEntity, LivroDto.class);
	}	
	
	public List<LivroDto> toDtoList(List<LivroEntity> livroEntityList){
		return modelMapper.map(livroEntityList, new TypeToken<List<LivroDto>>(){}.getType());
	}

	public LivroEntity toEntity(LivroDto livroDto) {
		return modelMapper.map(livroDto, LivroEntity.class);
	}

}
