/**
 * Problem Visibility API
 * 检查用户对问题资源的可见性
 * @description {哇}
 * @name: ProblemVisibility
 * @param {Document} problem - the Problem Document
 * @param {Document} user - the User Document
 * @author {zccz14}
 * @return {Promise<Boolean>}
 */
function ProblemVisibility(problem, user) {
  return new Promise((resolve, reject) => { resolve(true); })
}

module.exports = ProblemVisibility;