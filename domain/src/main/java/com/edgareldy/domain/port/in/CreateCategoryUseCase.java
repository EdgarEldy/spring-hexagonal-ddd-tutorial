package com.edgareldy.domain.port.in;

import com.edgareldy.domain.model.category.Category;
import com.edgareldy.domain.port.in.command.CreateCategoryCommand;

/**
 * Inbound port: create a category.
 * <p>
 * Created by edgar.muhamyangabo on 7/11/26
 * Author : edgar.muhamyangabo
 * Date : 7/11/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public interface CreateCategoryUseCase {

    Category createCategory(CreateCategoryCommand command);
}
