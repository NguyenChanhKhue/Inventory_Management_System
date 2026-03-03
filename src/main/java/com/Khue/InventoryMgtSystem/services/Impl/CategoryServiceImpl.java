package com.Khue.InventoryMgtSystem.services.Impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.Khue.InventoryMgtSystem.dto.CategoryDTO;
import com.Khue.InventoryMgtSystem.dto.Response;
import com.Khue.InventoryMgtSystem.exceptions.NotFoundException;
import com.Khue.InventoryMgtSystem.models.Category;
import com.Khue.InventoryMgtSystem.repository.CategoryRepository;
import com.Khue.InventoryMgtSystem.services.CategoryService;
import org.modelmapper.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	private final ModelMapper modelMapper;

	@Override
	public Response createCategory(CategoryDTO categoryDTO) {
		Category categoryToSave = modelMapper.map(categoryDTO, Category.class);

		categoryRepository.save(categoryToSave);

		return Response.builder()
				.status(200)
				.message("Category Created Successfully")
				.build();
	}

	@Override
	public Response getAllCategory() {
		List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

		categories.forEach(category -> category.setProducts(null));

		List<CategoryDTO> categoryDTOs = modelMapper.map(categories, new TypeToken<List<CategoryDTO>>() {
		}.getType());

		return Response.builder()
				.status(200)
				.message("Success")
				.categories(categoryDTOs)
				.build();
	}

	@Override
	public Response getCategoryById(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category Not Found"));

		CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

		return Response.builder()
				.status(200)
				.message("Success")
				.category(categoryDTO)
				.build();
	}

	@Override
	public Response updateCategory(Long id, CategoryDTO categoryDTO) {

		Category existingCategory = categoryRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Category Not Found"));

		if(categoryDTO.getName() != null) existingCategory.setName(categoryDTO.getName());
		categoryRepository.save(existingCategory);

		return Response.builder()
				.status(200)
				.message("Category Was Successfully Updated")
				.build();

	}

	@Override
	public Response deleteCategory(Long id) {
		categoryRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Category Not Found"));

		categoryRepository.deleteById(id);

		return Response.builder()
				.status(200)
				.message("Category Was Successfully Deleted")
				.build();
	}

}
