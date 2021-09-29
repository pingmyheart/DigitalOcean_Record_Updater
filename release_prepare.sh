#create release branch from develop
release=$(xmlstarlet sel -t -v //_:project/_:version pom.xml)
branch="release/v${release}"
echo -e ${branch}

git checkout develop
git checkout -b ${branch} develop
git push -u origin ${branch}
#merge develop on master
git checkout master
git merge develop
git push origin master
git checkout ${branch}
./dockerhub_deploy.sh
git cherckout develop