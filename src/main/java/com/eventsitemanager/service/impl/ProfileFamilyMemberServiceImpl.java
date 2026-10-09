package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ProfileFamilyMember;
import com.eventsitemanager.repository.ProfileFamilyMemberRepository;
import com.eventsitemanager.service.ProfileFamilyMemberService;
import com.eventsitemanager.service.dto.ProfileFamilyMemberDTO;
import com.eventsitemanager.service.mapper.ProfileFamilyMemberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileFamilyMemberServiceImpl implements ProfileFamilyMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileFamilyMemberServiceImpl.class);

    private final ProfileFamilyMemberRepository profileFamilyMemberRepository;
    private final ProfileFamilyMemberMapper profileFamilyMemberMapper;

    public ProfileFamilyMemberServiceImpl(
        ProfileFamilyMemberRepository profileFamilyMemberRepository,
        ProfileFamilyMemberMapper profileFamilyMemberMapper
    ) {
        this.profileFamilyMemberRepository = profileFamilyMemberRepository;
        this.profileFamilyMemberMapper = profileFamilyMemberMapper;
    }

    @Override
    public ProfileFamilyMemberDTO save(ProfileFamilyMemberDTO profileFamilyMemberDTO) {
        LOG.debug("Request to save ProfileFamilyMember : {}", profileFamilyMemberDTO);
        ProfileFamilyMember profileFamilyMember = profileFamilyMemberMapper.toEntity(profileFamilyMemberDTO);
        if (profileFamilyMember.getId() != null) {
            LOG.warn(
                "ProfileFamilyMember has ID {} set during create operation. Clearing ID to force sequence generation.",
                profileFamilyMember.getId()
            );
            profileFamilyMember.setId(null);
        }

        profileFamilyMember = profileFamilyMemberRepository.save(profileFamilyMember);
        return profileFamilyMemberMapper.toDto(profileFamilyMember);
    }

    @Override
    public ProfileFamilyMemberDTO update(ProfileFamilyMemberDTO profileFamilyMemberDTO) {
        LOG.debug("Request to update ProfileFamilyMember : {}", profileFamilyMemberDTO);
        ProfileFamilyMember profileFamilyMember = profileFamilyMemberMapper.toEntity(profileFamilyMemberDTO);

        profileFamilyMember = profileFamilyMemberRepository.save(profileFamilyMember);
        return profileFamilyMemberMapper.toDto(profileFamilyMember);
    }

    @Override
    public Optional<ProfileFamilyMemberDTO> partialUpdate(ProfileFamilyMemberDTO profileFamilyMemberDTO) {
        LOG.debug("Request to partially update ProfileFamilyMember : {}", profileFamilyMemberDTO);

        return profileFamilyMemberRepository
            .findById(profileFamilyMemberDTO.getId())
            .map(existing -> {
                profileFamilyMemberMapper.partialUpdate(existing, profileFamilyMemberDTO);

                return existing;
            })
            .map(profileFamilyMemberRepository::save)
            .map(profileFamilyMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileFamilyMemberDTO> findOne(Long id) {
        LOG.debug("Request to get ProfileFamilyMember : {}", id);
        return profileFamilyMemberRepository.findById(id).map(profileFamilyMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProfileFamilyMember : {}", id);
        profileFamilyMemberRepository.deleteById(id);
    }
}
