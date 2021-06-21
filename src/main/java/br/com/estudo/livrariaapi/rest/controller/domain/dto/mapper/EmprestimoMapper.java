package br.com.estudo.livrariaapi.rest.controller.domain.dto.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import br.com.estudo.livrariaapi.persistence.entity.EmprestimoEntity;
import br.com.estudo.livrariaapi.rest.controller.domain.dto.EmprestimoDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class EmprestimoMapper {

	private ModelMapper modelMapper;

	public EmprestimoDto toDto(EmprestimoEntity emprestimoEntity) {		
		return modelMapper.map(emprestimoEntity, EmprestimoDto.class);
	}	
	
	public List<EmprestimoDto> toDtoList(List<EmprestimoEntity> emprestimoEntityList){
		return modelMapper.map(emprestimoEntityList, new TypeToken<List<EmprestimoDto>>(){}.getType());
	}

	public EmprestimoEntity toEntity(EmprestimoDto emprestimoDto) {
		return modelMapper.map(emprestimoDto, EmprestimoEntity.class);
	}
	
}
