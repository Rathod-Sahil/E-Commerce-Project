package com.ecommerce.repositories;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.decorators.UserPaginationApi;
import com.ecommerce.models.User;

@RequiredArgsConstructor
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    @Override
    public List<User> filterByRole(List<String> roles){
        Query filter = filter(roles,new Query(softDelete()));
        return mongoTemplate.find(filter,User.class);
    }

    @Override
    public Page<User> filterSearchAndPagination(UserPaginationApi userPaginationApi){
        Query search = search(userPaginationApi.getUserFilter().getSearch(),new Query(softDelete()));
        Query filter = filter(userPaginationApi.getUserFilter().getRoles(), search);
        return pagination(userPaginationApi.getPagination().getPageNo(), userPaginationApi.getPagination().getPageSize(), filter);
    }

    //Filter query
    public Query filter(List<String> roles,Query query){
        if(roles==null){
            return query;
        }
        return query.addCriteria(Criteria.where("roles").in(roles));
    }

    //soft delete query
    public Criteria softDelete(){
        return Criteria.where("softDelete").is(false);
    }

    //Search query
    public Query search(String letter,Query query){
        if(letter==null){
            return query;
        }
        return  query.addCriteria(new Criteria().orOperator(
                Criteria.where("firstName").regex(".*"+letter+".*","i"),
                Criteria.where("lastName").regex(".*"+letter+".*","i"),
                Criteria.where("email").regex(".*"+letter+".*","i")));
    }

    //pagination
    public Page<User> pagination(int pageNo, int pageSize,Query query){
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        long totalElements = mongoTemplate.count(query, User.class);
        query.with(pageRequest);
        List<User> users = mongoTemplate.find(query, User.class);
        return new PageImpl<>(users, pageRequest, totalElements);
    }
 

}
