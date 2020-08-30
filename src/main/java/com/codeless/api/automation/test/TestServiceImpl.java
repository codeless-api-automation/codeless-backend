package com.codeless.api.automation.test;

import static java.util.stream.Collectors.toList;

import com.codeless.api.automation.function.Mapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

  @Autowired
  private Mapper<TestDto, Test> testDtoToTestMapper;
  @Autowired
  private Mapper<Test, TestDto> testToTestDtoMapper;
  @Autowired
  private TestRepository testRepository;

  @Override
  public TestDto saveTest(TestDto testDto) {
    return testToTestDtoMapper.map(testRepository.save(testDtoToTestMapper.map(testDto)));
  }

  @Override
  public PageDto<TestDto> getAllTests(Integer page, Integer size) {
    Page<Test> tests = testRepository.findAll(PageRequest.of(page, size));
    List<TestDto> dtoTests = tests
        .getContent()
        .stream()
        .map(testToTestDtoMapper::map)
        .collect(toList());
    return PageDto.<TestDto>builder()
        .size(tests.getSize())
        .number(tests.getNumber())
        .totalPages(tests.getTotalPages())
        .numberOfElements(tests.getNumberOfElements())
        .items(dtoTests)
        .build();
  }
}
