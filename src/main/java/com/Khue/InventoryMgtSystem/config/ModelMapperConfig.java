package com.Khue.InventoryMgtSystem.config;



import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper (){
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true) // map truc tiep field
      .setFieldAccessLevel(AccessLevel.PRIVATE) // cho phep truy cap field private
      .setMatchingStrategy(MatchingStrategies.STANDARD); // do tuong doi giua cac field
    
    return modelMapper;
  }
   
}
