package com.web.service;

import com.web.domain.Novel;
import com.web.repository.NovelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class NovelServiceImpl implements NovelService {

	private final NovelRepository novelRepository;

	public NovelServiceImpl(NovelRepository novelRepository) {
		this.novelRepository = novelRepository;
	}

    @Override
    public void addLikeCount(Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
        novel.setLikeCount(novel.getLikeCount() + 1);
        novelRepository.save(novel);
    }

    @Override
    public void subLikeCount(Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
        if (novel.getLikeCount() > 0) {
            novel.setLikeCount(novel.getLikeCount() - 1);
            novelRepository.save(novel);
        }
    }

	@Override
	public List<Novel> findAll(int page, int size) {
		List<Novel> novels = StreamSupport.stream(novelRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
		return paginate(novels, page, size);
	}

	@Override
	public List<Novel> findByCategory(String category, int page, int size) {
		List<Novel> novels = StreamSupport
				.stream(novelRepository.findByCategoryIgnoreCase(category).spliterator(), false)
				.collect(Collectors.toList());
		return paginate(novels, page, size);
	}

	@Override
	public long getTotalCount() {
		return novelRepository.count();
	}

	@Override
	public int getTotalPages(int size) {
		return (int) Math.ceil((double) novelRepository.count() / size);
	}

	@Override
	public void save(Novel novel) {
		novelRepository.save(novel);
	}
	
	 @Override
	    public void update(Long id, Novel updatedNovel) {
	        Novel novel = novelRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
	        novel.setTitle(updatedNovel.getTitle());
	        novel.setCategory(updatedNovel.getCategory());
	        novel.setDescription(updatedNovel.getDescription());
	        novelRepository.save(novel);
	    }

	    @Override
	    public void delete(Long id) {
	        novelRepository.deleteById(id);
	    }

	private List<Novel> paginate(List<Novel> novels, int page, int size) {
		int fromIndex = Math.min(page * size, novels.size());
		int toIndex = Math.min((page + 1) * size, novels.size());
		return novels.subList(fromIndex, toIndex);
	}

	@Override
    public Optional<Novel> findById(Long id) {
        return novelRepository.findById(id);
    }
}
