package com.blessing.rentaldream.modules.post.repository.querydsl;
import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.post.domain.QPost;
import com.blessing.rentaldream.modules.post.domain.QPostTag;
import com.blessing.rentaldream.modules.post.domain.QPostZone;
import com.blessing.rentaldream.modules.tag.QTag;
import com.blessing.rentaldream.modules.tag.Tag;
import com.blessing.rentaldream.modules.zone.Zone;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension {
    public PostRepositoryExtensionImpl() {
        super(Post.class);
    }

    @Override
    public Page<Post> findByKeyword(String keyword, Pageable pageable) {
        QPost post = QPost.post;
        QPostTag postTag= QPostTag.postTag;
        QTag tag = QTag.tag;
        JPQLQuery<Post> query = from(post).where(post.title.containsIgnoreCase(keyword)
                        .or(post.description.containsIgnoreCase(keyword))
                        .or(post.tags.any().tag.tagName.containsIgnoreCase(keyword))
                )
                .leftJoin(post.tags, postTag).fetchJoin()
                .leftJoin(postTag.tag,tag).fetchJoin()
                .distinct();
        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Post> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public Page<Post> findPostsByZoneListWithPaging(List<Zone> zoneList, Pageable pageable) {
        QPost post = QPost.post;
        JPQLQuery<Post> query = from(post).where(post.zones.any().zone.in(zoneList)).distinct();
        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Post> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public Page<Post> findPostsByZoneListAndTagListWithPaging(List<Tag> tagList, List<Zone> zoneList, Pageable pageable) {
        QPost post = QPost.post;
        JPQLQuery<Post> query = from(post).where(post.zones.any().zone.in(zoneList).and(post.tags.any().tag.in(tagList))).distinct();
        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Post> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
}
