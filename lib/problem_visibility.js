/**
 * Problem Visibility API
 * @description {检查用户对问题的可见性}
 * @name: ProblemVisibility
 * @param {Problem} problem - the Problem Document
 * @param {User} user - the User Document
 * @author zccz14 <zccz14@outlook.com>
 * @return {Promise<Boolean>}
 */
function ProblemVisibility(problem, user) {
  return new Promise((resolve, reject) => { resolve(true); })
}

module.exports = ProblemVisibility;