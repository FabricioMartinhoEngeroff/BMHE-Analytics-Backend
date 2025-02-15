package com.dvFabricio.BMEH;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
        "com.dvFabricio.BMEH.videoTest.domain",
        "com.dvFabricio.BMEH.videoTest.repository",
        "com.dvFabricio.BMEH.videoTest.service",
        "com.dvFabricio.BMEH.videoTest.controller"
})
public class AllTestsSuite {

}
