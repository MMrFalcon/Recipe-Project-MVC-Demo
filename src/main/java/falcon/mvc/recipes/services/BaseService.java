package falcon.mvc.recipes.services;

import org.springframework.data.repository.CrudRepository;


public interface BaseService<T extends Object, K extends Object, R extends CrudRepository<T,K>> {

}
