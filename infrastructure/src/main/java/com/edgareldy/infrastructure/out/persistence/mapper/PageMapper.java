package com.edgareldy.infrastructure.out.persistence.mapper;

import com.edgareldy.domain.model.shared.PageResult;
import com.edgareldy.domain.port.in.command.PageQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;

/**
 * Converts between the domain's framework-agnostic pagination types
 * ({@link PageQuery}/{@link PageResult}) and Spring Data's ({@link Pageable}/{@link Page}), so
 * that Spring's pagination types never leak into {@code domain} or {@code application}. Shared
 * by every {@code *RepositoryAdapter} that lists a paginated resource.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
public final class PageMapper {

    private PageMapper() {
    }

    public static Pageable toPageable(PageQuery query) {
        return PageRequest.of(query.page(), query.size());
    }

    public static <E, D> PageResult<D> toPageResult(Page<E> page, Function<E, D> toDomain) {
        List<D> content = page.getContent().stream().map(toDomain).toList();
        return new PageResult<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
