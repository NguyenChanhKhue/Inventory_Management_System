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
import com.Khue.InventoryMgtSystem.services.AuditLogService;
import org.modelmapper.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	private final ModelMapper modelMapper;

	private final AuditLogService auditLogService;

	@Override
	public Response createCategory(CategoryDTO categoryDTO) {
		Category categoryToSave = new Category();
		categoryToSave.setName(categoryDTO.getName());

		if (categoryDTO.getParentId() != null) {
			Category parent = categoryRepository.findById(categoryDTO.getParentId())
					.orElseThrow(() -> new NotFoundException("Parent Category Not Found"));
			categoryToSave.setParent(parent);
		}

		categoryRepository.save(categoryToSave);

		auditLogService.logAction("CREATE", "Category", categoryToSave.getId(), "Tạo mới danh mục: " + categoryToSave.getName());

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
		
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getParent() != null) {
				categoryDTOs.get(i).setParentId(categories.get(i).getParent().getId());
				categoryDTOs.get(i).setParentName(categories.get(i).getParent().getName());
			}
		}

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
		if (category.getParent() != null) {
			categoryDTO.setParentId(category.getParent().getId());
			categoryDTO.setParentName(category.getParent().getName());
		}

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
		
		if (categoryDTO.getParentId() != null) {
			Category parent = categoryRepository.findById(categoryDTO.getParentId())
					.orElseThrow(() -> new NotFoundException("Parent Category Not Found"));
			existingCategory.setParent(parent);
		}
		
		categoryRepository.save(existingCategory);

		auditLogService.logAction("UPDATE", "Category", existingCategory.getId(), "Cập nhật danh mục: " + existingCategory.getName());

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

		auditLogService.logAction("DELETE", "Category", id, "Xóa danh mục (ID: " + id + ")");

		return Response.builder()
				.status(200)
				.message("Category Was Successfully Deleted")
				.build();
	}

}
