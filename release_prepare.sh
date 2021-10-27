if ! git --version COMMAND &>/dev/null; then
    echo -e "git command could not be found"
    exit
fi
if ! xmlstarlet --version COMMAND &>/dev/null; then
    echo -e "xmlstarlet command could not be found"
    exit
fi
if ! mvn -v COMMAND &>/dev/null; then
    echo -e "mvn command could not be found"
    exit
fi
#create release branch from develop
release=$(xmlstarlet sel -t -v //_:project/_:version pom.xml)
branch="release/v${release}"
echo -e ${branch}
#push release branch
git checkout develop
git checkout -b ${branch} develop
git push -u origin ${branch}
#merge develop on master
git checkout master
git merge develop --no-edit
git push origin master
git checkout ${branch}
#build docker image and push
./dockerhub_deploy.sh
#return on develop
git checkout develop
next_release="${release%.*}.$((${release##*.}+1))"
mvn versions:set versions:commit -DnewVersion="${next_release}"
git add pom.xml
git commit -m "[AUTO] - Update pom.xml project version from v${release} to v${next_release}"
git push origin develop