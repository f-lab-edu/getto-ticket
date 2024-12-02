package com.flab.gettoticket.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestUtil {
    public static Pageable of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static Pageable of(int page, int size, Sort sort) {
        validateSize(size);
        return PageRequest.of(page, size, sort);
    }

    public static int getLimit(Pageable pageable) {
        return pageable.getPageSize();
    }

    public static long getOffset(Pageable pageable) {
        return pageable.getOffset();
    }

    private static void validateSize(int size) {
        if (size >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Integer.MAX_VALUE 보다 큰 값은 허용하지 않습니다.");
        }
    }
}
