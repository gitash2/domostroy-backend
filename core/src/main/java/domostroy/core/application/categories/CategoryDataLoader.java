package domostroy.core.application.categories;

import domostroy.core.adapters.adaptersOutput.categories.projections.CategoryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryDataLoader implements CommandLineRunner {
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() > 0) {
            return;
        }

        ClassPathResource classPathResource = new ClassPathResource("categories.csv");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;

            List<CategoryProjection> categories = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                String id = parts[0].trim();
                String name = parts[1].trim();
                categories.add(new CategoryProjection(Integer.valueOf(id), name));
            }
            categoryRepository.saveAll(categories);
        }
    }
}