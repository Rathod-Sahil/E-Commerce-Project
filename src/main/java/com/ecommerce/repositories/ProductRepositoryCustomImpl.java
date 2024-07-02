package com.ecommerce.repositories;

import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.decorators.ProductFilterDto;
import com.ecommerce.decorators.ProductPaginationApi;
import com.ecommerce.models.Product;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Product> filterSearchAndPagination(ProductPaginationApi productPaginationApi){
        Query search = search(productPaginationApi.getProductFilter().getSearch(),new Query(softDelete()));
        Query filterPrice = filterPrice(productPaginationApi.getProductFilter(), search);
        Query filterDiscountRate = filterDiscountRate(productPaginationApi.getProductFilter(), filterPrice);
        Query filterDate = filterDate(productPaginationApi.getProductFilter().getDate(), filterDiscountRate);
        Query filterStock = filterStock(productPaginationApi.getProductFilter(), filterDate);
        Query filterTopSelling = filterTopSelling(productPaginationApi.getProductFilter(), filterStock);
        return pagination(productPaginationApi.getPagination().getPageNo(), productPaginationApi.getPagination().getPageSize(), filterTopSelling);
    }

    @Override
    public long getAvailableProduct(Date date){
        Query softDelete = new Query(softDelete());
        Query filterDate = filterDate(date, softDelete);
        return mongoTemplate.count(filterDate, Product.class);
    }

    @Override
    public long getExpiredProduct(Date date){
        Query softDelete = new Query(softDelete());
        Query filterExpired = filterExpired(date, softDelete);
        return mongoTemplate.count(filterExpired, Product.class);
    }

    @Override
    public long getComingSoonProduct(Date date){
        Query softDelete = new Query(softDelete());
        Query filterComingSoon = filterComingSoon(date, softDelete);
        return mongoTemplate.count(filterComingSoon, Product.class);
    }

    //soft delete query
    public Criteria softDelete(){
        return Criteria.where("softDelete").is(false);
    }

    //Search query
    public Query search(String search,Query query){
        if(search==null){
            return query;
        }
        return  query.addCriteria(Criteria.where("name").regex(".*"+search+".*","i"));
    }

    //Filter Price
    public Query filterPrice(ProductFilterDto productFilterDto,Query query){
        if(productFilterDto.getMinPrice()==0 && productFilterDto.getMaxPrice()==0){
            return query;
        }
        return query.addCriteria(Criteria.where("price").gte(productFilterDto.getMinPrice()).lte(productFilterDto.getMaxPrice()));
    }

    //Filter discountRate
    public Query filterDiscountRate(ProductFilterDto productFilterDto,Query query){
        if(productFilterDto.getMinDiscountRate()==0 && productFilterDto.getMaxDiscountRate()==0){
            return query;
        }
        return query.addCriteria(Criteria.where("discountRate").gte(productFilterDto.getMinDiscountRate()).lte(productFilterDto.getMaxDiscountRate()));
    }

    //Date filter
    public Query filterDate(Date date, Query query){
        if(date==null){
            return query;
        }
        return query.addCriteria(Criteria.where("establishedDate").lte(date).andOperator(Criteria.where("endDate").gte(date)));
    }

    //Expired products
    public Query filterExpired(Date date, Query query){
        if(date==null){
            return query;
        }
        return query.addCriteria(Criteria.where("endDate").lt(date));
    }

    //Coming soon products
    public Query filterComingSoon(Date date, Query query){
        if(date==null){
            return query;
        }
        return query.addCriteria(Criteria.where("establishedDate").gt(date));
    }

    //stock filter
    public Query filterStock(ProductFilterDto productFilterDto, Query query){
        if(productFilterDto.getStock()==null){
            return query;
        }
        return query.addCriteria(Criteria.where("stock").is(productFilterDto.getStock()));
    }

    //Top-Selling filter
    public Query filterTopSelling(ProductFilterDto productFilterDto,Query query){
        if(productFilterDto.getTopSelling()==null){
            return query;
        }
        return query.addCriteria(Criteria.where("topSelling").is(productFilterDto.getTopSelling()));
    }

    //pagination
    public Page<Product> pagination(int pageNo, int pageSize,Query query){
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        long totalElements = mongoTemplate.count(query, Product.class);
        query.with(pageRequest);
        List<Product> products = mongoTemplate.find(query, Product.class);
        return new PageImpl<>(products, pageRequest,totalElements);
    }
 
}
