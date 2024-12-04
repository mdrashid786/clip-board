package com.clipbord.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clipbord.beans.Clipboards;

public interface ClipboardsRepository extends JpaRepository<Clipboards, String> {

	 Optional<Clipboards> findByContent(String content);

}
