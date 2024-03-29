#!/usr/bin/env groovy
pipeline {   
  agent none
  options {
    timeout(time: 1, unit: 'HOURS') 			// If the build takes longer than 1 hour, it is broken as FAILED
    timestamps()					// The timestamps are shown in every line of the console
    ansiColor('xterm')					// The console is shown coloured
  }
  environment {
    CI_SONAR=true 					//for running sonar always (not only at night), CI_SONAR=false does not run sonar at all
    http_proxy='http://proxy.vsa.de:8080'
    https_proxy='http://proxy.vsa.de:8080'
    no_proxy='.vsa.de,.ads-root.de,localhost'
    PHANTOMJS_CDNURL='http://webentw.intra.vsa.de/public/phantomjs/'
  }
  stages { 
    stage('Run Tests') {
        parallel {
            stage ('Tests On Windows') {
                agent {
                    label 'sam-windows'
                }

                environment {
                  JAVA_HOME = tool name: 'openjdk_15_windows'
                  PATH = "$JAVA_HOME/bin;$PATH"
                  CI_SONAR=false
                }

                steps {

                    dir("${env.WORKSPACE}"){
                        bat 'gradlew clean dockerInfo build -s'
                    }

                    dir("${env.WORKSPACE}/api"){
                        bat '..\\gradlew clean build -s'
                    }

                }
            }

            stage ('Tests On Linux') {
                agent {
                    label 'awinta'
                }

                environment {
                    JAVA_HOME = tool name: 'openjdk_15_linux'
                    PATH = "$JAVA_HOME/bin;$PATH"
                    CI_SONAR=false //TODO make true, when jacoco works again
                    CI_JACOCO=false
                }

                steps {
                    dir("${env.WORKSPACE}"){
                      sh './gradlew clean dockerInfo build -s'
                    }

                    dir("${env.WORKSPACE}/api"){
                        sh '../gradlew clean build -s'
                    }

                }

                post {
                    always {
                        //junit '**/build/test-results/**/TEST*.xml' 	// Collect junit reports to be shown in jenkins
                        archive '**/*.log'				// Archives are sent to the master server and can be viewed even if the workspace of the last build is removed
                        script {
                            currentBuild.description = GIT_BRANCH.replace ('origin/', '') // Show branch name in description
                            notifyBitbucket(
                                    commitSha1: GIT_COMMIT,
                                    credentialsId: '18db5f63-8f94-466f-9ef6-745f303ee6fa',
                                    disableInprogressNotification: false,
                                    considerUnstableAsSuccess: true,
                                    buildStatus: currentBuild.result,
                                    ignoreUnverifiedSSLPeer: true,
                                    projectKey: '',
                                    includeBuildNumberInKey: false,
                                    prependParentProjectKey: false,
                                    stashServerBaseUrl: 'https://stash.intra.vsa.de')
                        }
                        recordIssues(
                                enabledForFailure: true, aggregatingResults: true,
                                tools: [java(), checkStyle(pattern: '**/build/reports/checkstyle/main.xml', reportEncoding: 'UTF-8')]
                        )
                        mail to: 'markus.oley@vsa.de',		//Always send a mail after the build to this mail address
                                subject: "Pipeline ${currentBuild.fullDisplayName} finished with ${currentBuild.currentResult} ",
                                body: "Check ${BUILD_URL}"
                    }
                }
            }
        }
    }


    
  }
  

 
}

